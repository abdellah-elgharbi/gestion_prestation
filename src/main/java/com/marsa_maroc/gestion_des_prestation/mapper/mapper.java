package com.marsa_maroc.gestion_des_prestation.mapper;

import com.marsa_maroc.gestion_des_prestation.dto.PeseDto;
import com.marsa_maroc.gestion_des_prestation.entities.Pesees;
import com.marsa_maroc.gestion_des_prestation.entities.Prestation;
import com.marsa_maroc.gestion_des_prestation.enums.TypeOperation;
import com.marsa_maroc.gestion_des_prestation.enums.TypePesage;
import com.marsa_maroc.gestion_des_prestation.service.HistoriquePeseesService;
import com.marsa_maroc.gestion_des_prestation.service.PesageService;
import com.marsa_maroc.gestion_des_prestation.service.PrestationService;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class mapper {

    private final HistoriquePeseesService historiquePeseesService;
    private final PrestationService prestationService;

    public mapper(HistoriquePeseesService historiquePeseesService, PrestationService prestationService) {
        this.historiquePeseesService = historiquePeseesService;
        this.prestationService = prestationService;
    }

    public Pesees fromPesseDtoToPesse(PeseDto peseDto) {
        if (peseDto == null) {
            throw new IllegalArgumentException("El ne peux pas etre null");

        }
        Pesees pesees = new Pesees();
        pesees.setCamion(peseDto.getCamion());
        pesees.setPrestation(peseDto.getPresation());
        Prestation prestation = prestationService.getPrestationById(peseDto.getPresation().getNumeroPrestation());
        if (prestation.getTypeOperation().equals(TypeOperation.IMPORT)){


        if (TypePesage.TARE.equals(peseDto.getTypePesage())) {
            pesees.setDateTare(peseDto.getDateMesure());
            pesees.setPoidsTare(peseDto.getPoisMesurer());
            pesees.setHeureTare(peseDto.getHeureMesure());
        } else if (TypePesage.BRUT.equals(peseDto.getTypePesage())) {
            Pesees existingPeses = historiquePeseesService.getHistoriquePeseesByCamionLastOne(peseDto.getCamion().getImmatriculation(),peseDto.getPresation().getNumeroPrestation());
            if (existingPeses != null) {
                // Copy TARE data from existing record
                pesees.setDateTare(existingPeses.getDateTare());
                pesees.setPoidsTare(existingPeses.getPoidsTare());
                pesees.setHeureTare(existingPeses.getHeureTare());

            }
            pesees.setDateBrute(peseDto.getDateMesure());
            pesees.setPoidsBrut(peseDto.getPoisMesurer());
            pesees.setHeureBrute(peseDto.getHeureMesure());
            if (pesees.getPoidsTare() != null && pesees.getPoidsBrut() != null) {
                pesees.setPoidsNet(pesees.getPoidsBrut().subtract(pesees.getPoidsTare()));
                prestation.setPoisRestent(prestation.getPoidsDeclare().subtract(pesees.getPoidsNet()));
            }

             pesees.setIdHistoriquePesee(existingPeses.getIdHistoriquePesee());
            pesees.setPrestation(prestation);
            pesees.setBonSortie(true);


        }
        }
        else{
            if (TypePesage.BRUT.equals(peseDto.getTypePesage())) {
                pesees.setDateBrute(peseDto.getDateMesure());
                pesees.setPoidsBrut(peseDto.getPoisMesurer());
                pesees.setHeureBrute(peseDto.getHeureMesure());
            } else if (TypePesage.TARE.equals(peseDto.getTypePesage())) {
                Pesees existingPeses = historiquePeseesService.getHistoriquePeseesByCamionLastOne(peseDto.getCamion().getImmatriculation(),peseDto.getPresation().getNumeroPrestation());
                if (existingPeses != null) {
                    // Copy TARE data from existing record
                    pesees.setDateBrute(existingPeses.getDateBrute());
                    pesees.setPoidsBrut(existingPeses.getPoidsBrut());
                    pesees.setHeureBrute(existingPeses.getHeureBrute());

                }
                pesees.setDateTare(peseDto.getDateMesure());
                pesees.setPoidsTare(peseDto.getPoisMesurer());
                pesees.setHeureTare(peseDto.getHeureMesure());
                if (pesees.getPoidsTare() != null && pesees.getPoidsBrut() != null) {
                    pesees.setPoidsNet(pesees.getPoidsBrut().subtract(pesees.getPoidsTare()));
                    prestation.setPoisRestent(prestation.getPoidsDeclare().subtract(pesees.getPoidsNet()));
                }
                pesees.setIdHistoriquePesee(existingPeses.getIdHistoriquePesee());
                pesees.setPrestation(prestation);
                pesees.setBonSortie(true);
            }
        }
        return  pesees;

    }

}
