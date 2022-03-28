package com.esprit.projetpi.Services;


import com.esprit.projetpi.Entities.Reclamation;
import com.esprit.projetpi.Repositories.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceReclamation implements IServiceReclamation {


    private ReclamationRepository reclamationRepository;
    private ServiceMessages serviceMessages;

    public ServiceReclamation(ReclamationRepository reclamationRepo,ServiceMessages serviceMessages){
        this.reclamationRepository=reclamationRepo;
        this.serviceMessages=serviceMessages;
    }

    @Override
    public Boolean Create(Reclamation reclamation) {

        if (reclamation != null) {
            if(serviceMessages.profanityDetection(reclamation.getTitle()+"."+reclamation.getExplication())|| !serviceMessages.sentimentAnalysis(reclamation.getTitle()+"."+reclamation.getExplication())){
                return false;
            }
            reclamation.setTreatement(null);
            reclamation.setDateTime(LocalDateTime.now());
            reclamation.setState(false);
            reclamationRepository.save(reclamation);
            return true;

        }
        return false;
    }

    @Override
    public void Delete(int id) {
        reclamationRepository.deleteById(id);

    }

    @Override
    public boolean treat(int id, String treatement) {
        Reclamation reclamation = reclamationRepository.findById(id).orElse(null);
        if (reclamation != null && !reclamation.getState()) {
            reclamation.setTreatement(treatement);
            reclamation.setState(true);
            reclamationRepository.save(reclamation);
            serviceMessages.sendMail("Your claim number "+reclamation.getId()+" have been treated \n ", reclamation.getTreatement(),"saber.herbegue@esprit.tn");

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Reclamation getOne(int id) {
        return reclamationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reclamation> findAll() {
        return reclamationRepository.findAll(Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    public List<Reclamation> findNotTreated() {
        return reclamationRepository.findNotTreated();
    }


    @Override
    public List<Reclamation> filter(String filter) {
        return reclamationRepository.findByFilter(filter);
    }

    @Override
    public List<Reclamation> findBetweenDate(LocalDateTime start, LocalDateTime end) {
        return reclamationRepository.findBetweenDate(start, end);
    }

    @Override
    public List<Reclamation> findByType(String type) {
        return reclamationRepository.findByType(type);
    }


    public List<Reclamation> searchMultiCriteria(String keyword, String type, LocalDateTime start, LocalDateTime end, boolean treated) {

        List<Reclamation> reclamations;

        //fetch all claims
        reclamations = findAll();


        //if there is filter by keyword then do it using stream
        if (keyword!=null && !keyword.isEmpty()) {
            reclamations = reclamations.stream().filter(x -> x.getTitle().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
        }
        //if there is filter by type then do it using stream
        if (type!=null && !type.isEmpty()) {
            reclamations = reclamations.stream().filter(x -> x.getType().toLowerCase().contains(type.toLowerCase())).collect(Collectors.toList());
        }
        //if the start and the end date specified filter using stream
        if (start != null && end != null) {
            reclamations = reclamations.stream().filter(x -> x.getDateTime().isAfter(start) && x.getDateTime().isBefore(end)).collect(Collectors.toList());
        }
        //if treated is true then filter only the treated ones
        if (treated) {
            reclamations = reclamations.stream().filter(Reclamation::getState).collect(Collectors.toList());
        }

        return reclamations;


    }
}