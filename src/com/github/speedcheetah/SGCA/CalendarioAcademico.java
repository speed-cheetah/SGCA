/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.speedcheetah.SGCA;

import java.util.ArrayList;
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
            System.out.println("4 - Pesquisar por nome.");

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
                    
                    Regionais.CATALAO.mostrarMenu();
                    int numRegional;
                    try {
                        numRegional = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException ex) {
                        System.out.println("Número inválido.");
                        break;
                    }
                    String escolhaRegional
                            = Regionais.CATALAO.escolhaRegional(numRegional);
                    repositorio.exibirRegional(eventos, escolhaRegional);
                    break;
                case 4 :
                    System.out.println("Digite o nome do evento:");
                    String nome = scan.nextLine();
                    repositorio.exibirNome(eventos, nome);
                default:
                    break;
            }

        }
        System.out.println("Fim");
        System.exit(0);
    }

}
