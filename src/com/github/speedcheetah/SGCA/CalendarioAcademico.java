/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.speedcheetah.SGCA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author saulognome
 */
public class CalendarioAcademico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList eventos = new ArrayList();
        RepositorioEvento repositorio = new RepositorioEvento();

        System.out.println("##### Cadastro de eventos #####\n");

        int opcao = 1;
        Scanner scan = new Scanner(System.in);

        while (opcao != 0) {
            System.out.println("0 - Sair do programa.");
            System.out.println("1 - Cadastrar evento novo.");
            System.out.println("2 - Exibir calendário inteiro.");
            System.out.println("3 - Pesquisar por regional.");

            try {
                opcao = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException ex) {
                opcao = 10;
            }

            switch (opcao) {
                case 0:
                    System.out.println("Fim");
                    System.exit(0);
                    break;

                case 1:
                    repositorio.addEvento(eventos);
                    Collections.sort(eventos);
                    break;

                case 2:
                    eventos.forEach(System.out::println);
                    break;

                case 3:
                    System.out.println("Qual(is) a(s) regional(is) que deseja"
                            + " procurar? (Separe por vírgula.)");
                    String entrada = scan.nextLine();
                    ArrayList<String> regionais = repositorio.parseString(entrada);
                    repositorio.exibirRegional(eventos, regionais);
                    break;

                default:
                    break;
            }

        }

        System.out.println("Fim");
        System.exit(0);
    }

}
