/*
 * The MIT License
 *
 * Copyright 2016 Instituto de Informática (UFG) - Fábrica de Software.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.speedcheetah.SGCA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saulognome
 */
public class CalendarioAcademico {

    public static Scanner scan = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArrayList eventos = new ArrayList();
        RepositorioEvento repositorio = new RepositorioEvento();
        Administrador admin = new Administrador("admin", "password");
        admin.logoff();

        System.out.println("##### Cadastro de eventos #####\n");

        int opcao = 1;

        while (opcao != 0) {
            String user, pass;
            System.out.println("0 - Sair do programa.");
            System.out.println("1 - Exibir calendário inteiro.");
            System.out.println("2 - Pesquisar por regional.");
            System.out.println("3 - Pesquisar por nome.");
            System.out.println("4 - Pesquisar por data.");
            System.out.println("5 - Registrar Administrador.");
            System.out.println("6 - Fazer login.");
            if (admin.isOnline()) {
                System.out.println("7 - Cadastrar evento novo.");
                System.out.println("8 - Remover um evento.");
                System.out.println("9 - Alterar um evento.");
                System.out.println("10 - Alterar senha.");
            }

            try {
                opcao = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
                opcao = -1;
            }

            switch (opcao) {
                case 0:
                    System.out.println("Fim");
                    System.exit(0);
                    break;

                case 1:
                    eventos.forEach(System.out::println);
                    break;

                case 2:

                    Regionais.CATALAO.mostrarMenu();
                    int numRegional;
                    try {
                        numRegional = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        break;
                    }
                    String escolhaRegional
                            = Regionais.CATALAO.escolhaRegional(numRegional);
                    ArrayList<Evento> pesquisa;
                    try {
                        pesquisa = repositorio
                                .pesquisarRegional(eventos, escolhaRegional);
                        pesquisa.forEach(System.out::println);
                    } catch (EventoNaoLocalizadoException ex) {
                        System.out.println(ex.getMessage());
                        Logger.getLogger(CalendarioAcademico.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    break;

                case 3:
                    System.out.print("Digite o nome do evento: ");
                    String nome = scan.nextLine();
                    Evento ev;
                    try {
                        ev = repositorio.pesquisarNome(eventos, nome);
                        System.out.println(ev.toString());
                    } catch (EventoNaoLocalizadoException ex) {
                        System.out.println(ex.getMessage());
                        Logger.getLogger(CalendarioAcademico.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    break;

                case 4:
                    System.out.print("Digite a data desejada: ");
                    String data = scan.nextLine();
                    String dataValor[] = RepositorioEvento.parseData(data);
                    GregorianCalendar dataPesquisa = new GregorianCalendar();
                    try {
                        dataPesquisa.set(Integer.parseInt(dataValor[2]),
                                Integer.parseInt(dataValor[1]) - 1,
                                Integer.parseInt(dataValor[0]));
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        dataPesquisa.set(-1, -1, -1);
                    }
                     {
                        try {
                            repositorio.pesquisarData(eventos, dataPesquisa);
                        } catch (EventoNaoLocalizadoException ex) {
                            System.out.println(ex.getMessage());
                            Logger.getLogger(CalendarioAcademico.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                case 5:
                    System.out.print("Confirme o usuário pré-cadastrado: ");
                    user = scan.nextLine();
                    System.out.print("Confirme a senha pré-cadastrada: ");
                    pass = scan.nextLine();
                    admin.login(user, pass);
                    if (admin.isOnline()) {
                        System.out.print("Digite o novo nome de usuário: ");
                        user = scan.nextLine();
                        System.out.print("Digite a nova senha: ");
                        pass = scan.nextLine();
                        admin = new Administrador(user, pass);
                    } else {
                        System.out.println("Usuário ou senha incorreto.");
                    }
                    break;

                case 6:
                    System.out.print("Digite o nome de usuário: ");
                    user = scan.nextLine();
                    System.out.print("Digite a senha: ");
                    pass = scan.nextLine();
                    admin.login(user, pass);
                    if (admin.isOnline()) {
                        System.out.println("Bem vindo "
                                + admin.getUser() + "!");
                    } else {
                        System.out.println("Usuário ou senha incorreto.");
                    }
                    break;

                case 7:
                    if (admin.isOnline()) {
                        try {
                            Evento newEvento = lerEvento();
                            repositorio.addEvento(eventos, newEvento);
                            System.out.println("Evento adicionado.");
                            Collections.sort(eventos);
                        } catch (EventoDuplicadoException ex) {
                            System.out.println(ex.getMessage());
                            Logger.getLogger(CalendarioAcademico.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                case 8:
                    if (admin.isOnline()) {
                        System.out.println("Digite o nome do evento a ser"
                                + "removido");
                        String nomePesquisa = scan.nextLine();
                        try {
                            repositorio.removerEvento(eventos, nomePesquisa);
                        } catch (EventoNaoLocalizadoException ex) {
                            System.out.println(ex.getMessage());
                            Logger.getLogger(CalendarioAcademico.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                        Collections.sort(eventos);
                    }
                    break;

                case 9:
                    if (admin.isOnline()) {
                        System.out.println("Digite o nome do evento a ser "
                                + "alterado");
                        String nomePesquisa = scan.nextLine();
                        try {
                            repositorio.alterarEvento(eventos, nomePesquisa);
                            Collections.sort(eventos);
                        } catch (EventoDuplicadoException
                                | EventoNaoLocalizadoException ex) {
                            System.out.println(ex.getMessage());
                            Logger.getLogger(CalendarioAcademico.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                    break;

                case 10:
                    if (admin.isOnline()) {
                        System.out.print("Confirme sua senha: ");
                        pass = scan.nextLine();
                        if (admin.confirmaSenha(pass)) {
                            System.out.print("Entre com a nova senha: ");
                            String newPass = scan.nextLine();
                            admin.alteraSenha(pass, newPass);
                        }
                    }
                    break;

                default:
                    break;
            }

        }
        System.out.println("Fim");
        System.exit(0);
    }

    public static int menuModificacao() {
        System.out.println("O que deseja alterar?");
        System.out.println("1 - Nome.");
        System.out.println("2 - Data de inicio.");
        System.out.println("3 - Data de término.");
        System.out.println("4 - Regional.");
        System.out.println("5 - Instituto.");
        System.out.println("6 - Descrição.");
        System.out.println("0 - Terminar as alterações");

        int opcao;
        try {
            opcao = Integer.parseInt(scan.nextLine());
        } catch (NumberFormatException ex) {
            opcao = -1;
        }
        return opcao;
    }

    public static Evento lerEvento() throws EventoDuplicadoException {
        String nome = cadastrarNome();

        System.out.print("Data de início(dd/mm/aaaa): ");
        GregorianCalendar dataI = cadastrarData();

        System.out.print("Data de final(dd/mm/aaaa): ");
        GregorianCalendar dataFinal = cadastrarData();

        Evento evento = new Evento(nome, dataI, dataFinal,
                cadastrarRegional(), cadastrarInstituto(),
                cadastrarDescricao());

        return evento;
    }

    public static String cadastrarNome() {
        System.out.println("Digite o nome do Evento:");
        String nome = scan.nextLine();
        return nome.toUpperCase();
    }

    public static ArrayList cadastrarRegional() throws EventoDuplicadoException {
        String maisUm = "s";
        int cont = 0;
        ArrayList<String> regionalList = new ArrayList();
        while ("s".equalsIgnoreCase(maisUm) && cont < 4) {

            Regionais.CATALAO.mostrarMenu();

            int numRegional = Integer.parseInt(scan.nextLine());

            String escolhaRegional
                    = Regionais.CATALAO.escolhaRegional(numRegional);

            if (regionalList.contains(escolhaRegional)) {
                throw new EventoDuplicadoException("Regional já consta"
                        + " cadastrada para esse evento.");
            }
            System.out.println("Deseja cadastrar mais uma regional"
                    + " para o evento?");
            maisUm = scan.nextLine();

            cont++;
        }

        return regionalList;
    }

    public static String cadastrarInstituto() {
        System.out.print("Instituto: ");
        String instituto = scan.nextLine();
        return instituto.toUpperCase();
    }

    public static String cadastrarDescricao() {
        System.out.print("Descrição: ");
        String descricao = scan.nextLine();
        return descricao.toUpperCase();
    }

    public static GregorianCalendar cadastrarData() {

        GregorianCalendar dataInicial = new GregorianCalendar();
        dataInicial.setLenient(false);
        do {
            String data = scan.nextLine();
            String[] dataValor = RepositorioEvento.parseData(data);
            try {
                dataInicial.set(Integer.parseInt(dataValor[2]),
                        Integer.parseInt(dataValor[1]) - 1,
                        Integer.parseInt(dataValor[0]));
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                dataInicial.set(-1, -1, -1);
            }
        } while (!RepositorioEvento.testaData(dataInicial));

        return dataInicial;
    }
}
