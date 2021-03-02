package hr.java.covidportal.main;

import hr.java.covidportal.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Glavna {

    private static final Integer BROJ_SIMPTOMA = 3;
    private static final Integer BROJ_BOLESTI = 4;
    private static final Integer BROJ_OSOBA = 3;


    public static void main(String[] args) {

        Scanner unos = new Scanner(System.in);
        ArrayList<Zupanija> zupanije = new ArrayList<Zupanija>();
        Simptom[] simptomi = new Simptom[BROJ_SIMPTOMA];
        Bolest[] bolesti = new Bolest[BROJ_BOLESTI];
        Osoba[] osobe = new Osoba[BROJ_OSOBA];

        //1. ZADATAK
        Integer indeks = 0;
        System.out.println("UNOS ŽUPANIJA");
        while(1 > 0){
            System.out.print("Unesite naziv " + (indeks + 1) + ". županije: ");
            String naziv = unos.nextLine();
            if(naziv.isEmpty()) {
                break;
            }
            System.out.print("Unesite broj stavnovnika " + (indeks + 1) + ". županije: ");
            Integer brojStanovnika = unos.nextInt();
            unos.nextLine();
            Zupanija zupanija = new Zupanija(naziv, brojStanovnika);
            zupanije.add(zupanija);
            indeks++;
        }

        System.out.println("\nUNOS SIMPTOMA");
        for(int i = 0; i < BROJ_SIMPTOMA; i++){
            simptomi[i] = unesiSimptom(unos, i);
        }

        System.out.println("\nUNOS BOLESTI/VIRUSA");
        for(int i = 0; i < BROJ_BOLESTI; i++){
            bolesti[i] = unesiBolest(unos, simptomi, i);
        }
        System.out.println("\nUNOS OSOBA");
        for(int i = 0; i < BROJ_OSOBA; i++){
            osobe[i] = unesiOsobu(unos, zupanije, bolesti, osobe, i);
            System.out.println("\n");
        }


        //2. ZADATAK
        for(int i = 0; i < zupanije.size(); i++) {
            for (int j = 0; j < zupanije.size(); j++) {
                if (zupanije.get(j).getNaziv().compareTo(zupanije.get(i).getNaziv()) > 0 ) {
                    Zupanija tmp = zupanije.get(i);
                    zupanije.set(i, zupanije.get(j));
                    zupanije.set(j, tmp);
                }

                else if (zupanije.get(j).getNaziv().compareTo(zupanije.get(i).getNaziv()) == 0 ) {
                    Zupanija tmp2 = zupanije.get(i);
                    if(zupanije.get(j).getBrojStanovnika().compareTo(zupanije.get(i).getBrojStanovnika()) > 0 )
                    tmp2 = zupanije.get(i);
                    zupanije.set(i, zupanije.get(j));
                    zupanije.set(j, tmp2);
                }
            }
        }

        Ispis(osobe);

        //3. ZADATAK
        System.out.println("\nPOPIS ŽUPANIJA:");
        for(int i = 0; i < zupanije.size(); i++){
            System.out.println(zupanije.get(i).getNaziv() + " " + zupanije.get(i).getBrojStanovnika());
        }
    }

    private static Osoba unesiOsobu(Scanner unos, ArrayList<Zupanija> zupanije, Bolest[] bolesti, Osoba[] osobe, int i) {
        System.out.println("Unesite podatke " + (i + 1) + ". osobe.");
        System.out.print("Unesite ime osobe: ");
        String ime = unos.nextLine();
        System.out.print("Unesite prezime osobe: ");
        String prezime = unos.nextLine();
        System.out.print("Unesite starost osobe: ");
        Integer starost = unos.nextInt();
        unos.nextLine();

        System.out.println("Unesite županiju prebivališta osobe: ");
        Zupanija zupanija = odaberiZupaniju(unos, zupanije);

        System.out.println("Unesite bolest osobe: ");
        Bolest zarazenBolescu = odaberiBolest(unos, bolesti);

        Osoba[] kontakti = odaberiKontakte(unos, osobe, i);
        return new Osoba.Builder()
                .withIme(ime)
                .withPrezime(prezime)
                .withStarost(starost)
                .withZupanija(zupanija)
                .withZarazenBolescu(zarazenBolescu)
                .withKontaktiraneOsobe(kontakti)
                .build();
    }

    private static Osoba[] odaberiKontakte(Scanner unos, Osoba[] osobe, int i){
        if(i == 0){
            Osoba[] prviZarazeni = new Osoba[0];
            return prviZarazeni;
        }
        else {
            System.out.print("Unesite broj osoba koje su bile u kontaktu s tom osobom: ");
            Integer brojKontaktiranihOsoba = unos.nextInt();
            unos.nextLine();
            Osoba[] kontaktiraneOsobe = new Osoba[brojKontaktiranihOsoba];
            Integer odabranaOsoba = 0;
            boolean bilaOsoba = false; //PROVJERAVA JESMO LI TU OSOBU VEĆ OZNAČILI KAO KONTAKT
            for (int j = 0; j < brojKontaktiranihOsoba; j++) {
                System.out.println("Odaberite " + (j + 1) + ". osobu koja je bila u kontaktu:");
                do {
                    bilaOsoba = false;
                    for (int k = 0; k < i; k++) { //ISPIS SVIH OSOBA DO TE OSOBE
                        System.out.println((k + 1) + ". " + osobe[k].getIme() + " " + osobe[k].getPrezime());
                    }
                    System.out.print("Odabir: ");
                    odabranaOsoba = unos.nextInt();
                    unos.nextLine();
                    if (odabranaOsoba < 1 || odabranaOsoba > i) {
                        System.out.println("Neispravan unos! Pokušajte ponovno.");
                    }
                    else if(j > 0) {
                        if (osobe[odabranaOsoba - 1].getIme() == kontaktiraneOsobe[j - 1].getIme() &&
                                osobe[odabranaOsoba - 1].getPrezime() == kontaktiraneOsobe[j - 1].getPrezime() &&
                                osobe[odabranaOsoba - 1].getStarost() == kontaktiraneOsobe[j - 1].getStarost() &&
                                osobe[odabranaOsoba - 1].getZupanija() == kontaktiraneOsobe[j - 1].getZupanija()){
                            System.out.println("Već ste unijelu tu osobu kao kontakt! Pokušajte ponovno.");
                            bilaOsoba = true;
                        }
                    }
                } while (odabranaOsoba < 1 || odabranaOsoba > i || bilaOsoba) ;
                kontaktiraneOsobe[j] = osobe[odabranaOsoba - 1];
            }
            return kontaktiraneOsobe;
        }
    }

    private static Bolest odaberiBolest(Scanner unos, Bolest[] bolesti) {
        Integer odabranaBolest = 0;
        Bolest zarazenBolescu;
        do {
            for (int j = 0; j < BROJ_BOLESTI; j++) { //ISPIS SVIH BOLESTI
                System.out.println((j + 1) + ". " + bolesti[j].getNaziv());
            }
            System.out.print("Odabir: ");
            odabranaBolest = unos.nextInt();
            unos.nextLine();
            if(odabranaBolest < 1 || odabranaBolest > BROJ_BOLESTI){
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        }while(odabranaBolest < 1 || odabranaBolest > BROJ_BOLESTI);
        zarazenBolescu = bolesti[odabranaBolest - 1];
        return zarazenBolescu;
    }

    private static Zupanija odaberiZupaniju(Scanner unos, ArrayList<Zupanija> zupanije) {
        Integer odabranaZupanija = 0;
        Zupanija zupanija;
        do {
            for (int j = 0; j < zupanije.size(); j++) {
                System.out.println((j + 1) + ". " + zupanije.get(j).getNaziv());
            }
            System.out.print("Odabir: ");
            odabranaZupanija = unos.nextInt();
            unos.nextLine();
            if(odabranaZupanija < 1 || odabranaZupanija > zupanije.size()){
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        }while(odabranaZupanija < 1 || odabranaZupanija > zupanije.size());
        zupanija = zupanije.get(odabranaZupanija - 1);
        return zupanija;
    }

    private static void Ispis(Osoba[] osobe) {
        System.out.println("\n------POPIS OSOBA------");
        for(int i = 0; i < BROJ_OSOBA; i++){
            System.out.println("\n" + (i + 1) + ". OSOBA:");
            System.out.println("Ime i prezime: " + osobe[i].getIme() + " " + osobe[i].getPrezime());
            System.out.println("Starost: " + osobe[i].getStarost());
            System.out.println("Županija prebivališta: " + osobe[i].getZupanija().getNaziv());
            System.out.println("Zaražen bolešću: " + osobe[i].getZarazenBolescu().getNaziv());
            if(i == 0 || osobe[i].getKontaktiraneOsobe().length == 0){
                System.out.println("Nema kontaktiranih osoba.");
            }
            else{
                System.out.println("Kontaktirane osobe:");
                for (int j = 0; j < osobe[i].getKontaktiraneOsobe().length; j++) {
                    System.out.println("- " + osobe[i].getKontaktiraneOsobe()[j].getIme() + " " +
                            osobe[i].getKontaktiraneOsobe()[j].getPrezime());
                }
            }
        }
    }

    private static Bolest unesiBolest(Scanner unos, Simptom[] simptomi, int i) {
        System.out.println("Unosite li bolest ili virus?\n1) BOLEST\n2) VIRUS");
        System.out.print("Odabir: ");
        Integer odabirBolestIliVirus = unos.nextInt();
        unos.nextLine();
        System.out.print("Unesite naziv " + (i + 1) + ". bolesti/virusa: ");
        String naziv = unos.nextLine();
        System.out.print("Unesite broj simptoma " + (i + 1) + ". bolesti/virusa: ");
        Integer brojSimptomaBolesti = unos.nextInt();
        unos.nextLine();
        Simptom[] simptomiBolesti = new Simptom[brojSimptomaBolesti];
        for(int j = 0; j < brojSimptomaBolesti; j++){
            Integer indexOdabranogSimptoma = odaberiSimptom(unos, simptomi, j);
            simptomiBolesti[j] = simptomi[indexOdabranogSimptoma - 1];
        }

        if(odabirBolestIliVirus == 1){
            return new Bolest(naziv, simptomiBolesti);
        }
        else{
            return new Virus(naziv, simptomiBolesti);
        }

    }

    private static Integer odaberiSimptom(Scanner unos, Simptom[] simptomi, int j) {
        Integer odabraniSimptom = 0;
        do {
            System.out.println("Odaberite " + (j + 1) + ". simptom: ");
            for(int k = 0; k < BROJ_SIMPTOMA; k++){ //ISPIS SVIH SIMPTOMA
                System.out.println((k + 1) + ". " + simptomi[k].getNaziv() + " " + simptomi[k].getVrijednost());
            }
            System.out.print("Odabir: ");
            odabraniSimptom = unos.nextInt();
            unos.nextLine();
            if((odabraniSimptom < 1 || odabraniSimptom > BROJ_SIMPTOMA)){
                System.out.println("Neispravan unos, molimo pokušajte ponovno!");
            }
        }while(odabraniSimptom < 1 || odabraniSimptom > BROJ_SIMPTOMA);
        return odabraniSimptom;
    }

    private static Simptom unesiSimptom(Scanner unos, int i) {
        System.out.print("Unesite naziv " + (i + 1) + ". simptoma: ");
        String naziv = unos.nextLine();
        System.out.print("Unesite vrijednost " + (i + 1) + ". simptoma (RIJETKO, SREDNJE ILI ČESTO): ");
        String vrijednost = unos.nextLine();
        Simptom noviSimptom = new Simptom(naziv, vrijednost);
        return noviSimptom;
    }

    //private static Zupanija unesiZupaniju(Scanner unos, int indeks) {
        //System.out.print("Unesite naziv " + (indeks + 1) + ". županije: ");
        //String naziv = unos.nextLine();
        //System.out.print("Unesite broj stavnovnika " + (indeks + 1) + ". županije: ");
        //Integer brojStanovnika = unos.nextInt();
        //unos.nextLine();
        //Zupanija zupanija = new Zupanija(naziv, brojStanovnika);
        //return zupanija;
    //}
}