package com.marsa_maroc.gestion_des_prestation.service;


import com.marsa_maroc.gestion_des_prestation.entities.Pesees;
import com.marsa_maroc.gestion_des_prestation.entities.Prestation;
import com.marsa_maroc.gestion_des_prestation.exeptions.ResourceNotFoundException;
import com.marsa_maroc.gestion_des_prestation.respositories.HistoriquePeseesRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class HistoriquePeseesService {

    private final HistoriquePeseesRepository historiquePeseesRepository;


    public Pesees getLastHistTare(String numeroDePrestation){
        System.out.println("gggg");
        return historiquePeseesRepository.findFirstByDateBruteAndPrestationNumeroPrestationOrderByIdHistoriquePeseeDesc(null,numeroDePrestation);
    }
    @Transactional(readOnly = true)
    public List<Pesees> getAllHistoriquePesees() {
        return historiquePeseesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pesees getHistoriquePeseeById(Integer id) {
        return historiquePeseesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historique pesée non trouvé avec l'ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pesees> getHistoriquePeseesByPrestation(String numeroPrestation) {
        return historiquePeseesRepository.findByPrestationNumeroPrestation(numeroPrestation);
    }

    @Transactional(readOnly = true)
    public List<Pesees> getHistoriquePeseesByCamion(String immatriculation) {
        return historiquePeseesRepository.findByCamionImmatriculation(immatriculation);
    }
    @Transactional(readOnly = true)
    public Pesees getHistoriquePeseesByCamionLastOne(String immatriculation,String numeroPrestation) {
        return historiquePeseesRepository.findFirstByCamionImmatriculationAndPrestationNumeroPrestationOrderByIdHistoriquePeseeDesc(immatriculation,numeroPrestation);
    }


    @Transactional(readOnly = true)
    public List<Pesees> getHistoriquePeseesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return historiquePeseesRepository.findByDateTareBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Pesees> getHistoriquePeseesByPrestationOrderByDate(String numeroPrestation) {
        return historiquePeseesRepository.findByPrestationOrderByDateTareDesc(numeroPrestation);
    }

    public Pesees createHistoriquePesee(Pesees historiquePesee) {
        return historiquePeseesRepository.save(historiquePesee);
    }

    public Pesees updateHistoriquePesee(Integer id, Pesees historiquePeseeDetails) {
        Pesees historiquePesee = getHistoriquePeseeById(id);

        historiquePesee.setPrestation(historiquePeseeDetails.getPrestation());
        historiquePesee.setCamion(historiquePeseeDetails.getCamion());
        historiquePesee.setDateTare(historiquePeseeDetails.getDateTare());
        historiquePesee.setHeureTare(historiquePeseeDetails.getHeureTare());
        historiquePesee.setPoidsBrut(historiquePeseeDetails.getPoidsBrut());
        historiquePesee.setPoidsTare(historiquePeseeDetails.getPoidsTare());
        historiquePesee.setPoidsNet(historiquePeseeDetails.getPoidsNet());
        historiquePesee.setTarif(historiquePeseeDetails.getTarif());
        historiquePesee.setBonSortie(historiquePeseeDetails.getBonSortie());

        return historiquePeseesRepository.save(historiquePesee);
    }

    public void deleteHistoriquePesee(Integer id) {
        Pesees historiquePesee = getHistoriquePeseeById(id);
        historiquePeseesRepository.delete(historiquePesee);
    }
}