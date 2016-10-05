/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Helper.Helper;
import Fake.FakeFile;
import Fake.FakeFileSystem;
import Fake.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import operatingSystem.Kernel;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Kernel desenvolvido pelo aluno. Outras classes criadas pelo aluno podem ser
 * utilizadas, como por exemplo: - Arvores; - Filas; - Pilhas; - etc...
 *
 * @author nome do aluno...
 */
public class MyKernel implements Kernel {

    public FakeFileSystem fs;

    public MyKernel() {
        this.fs = new FakeFileSystem();
    }

    public String ls(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        String dest = null, opt = null;
        if (!parameters.equals("")) {
            String[] psplit = parameters.split(" ");
            if (psplit.length == 2) {
                if (psplit[1].subSequence(0, 1).equals("-")) {
                    opt = psplit[1];
                    dest = psplit[0];
                } else if (psplit[0].subSequence(0, 1).equals("-")) {
                    opt = psplit[0];
                    dest = psplit[1];
                }
            } else if (psplit.length == 1) {
                if (psplit[0].subSequence(0, 1).equals("-")) {
                    opt = psplit[0];
                    dest = "./";
                } else {
                    opt = null;
                    dest = psplit[0];
                }
            } else {
                dest = "./";
            }
        } else {
            dest = "./";
        }
        String op_path = Helper.getOperatingPath(dest);
        FakeFile f = fs.pathToFakeFile(op_path);
        SimpleDateFormat dt = new SimpleDateFormat("MMM d hh:mm", Locale.US);
        FakeFile[] files = (FakeFile[]) f.listFiles();
        if (opt != null && opt.contains("l")) {
            for (FakeFile file : files) {
                try {
                    result += PosixFilePermissions.toString(Helper.intToSetPosix(file.chmod))
                            + "\t" + file.owner
                            + "\t" + dt.format(file.lastModified())
                            + "\t" + file.getName() + "\n";
                } catch (Exception e) {
                    result = "Erro: " + file.getName() + ": " + e.getMessage();
                }
            }
        } else {
            for (File file : files) {
                result += file.getName() + "\n";
            }
        }
        //fim da implementacao do aluno

        return result;
    }

    public String mkdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: mkdir");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        String dir = Helper.getOperatingPath(parameters);
        FakeFile f = new FakeFile(fs, dir);
        if (!f.exists()) {
            f.mkdirs();
        } else {
            result = "mkdir: " + parameters + ": Diretorio ja existe (Nenhum diretorio foi criado).";
        }
        //fim da implementacao do aluno
        return result;
    }

    public String cd(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        String currentDir = "";
        System.out.println("Chamada de Sistema: cd");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //indique o diretório atual. Por exemplo... /
        if (parameters.equals("/") || parameters.equals("")) {
            operatingSystem.fileSystem.FileSytemSimulator.currentDir = "/";
        } else {
            currentDir = Helper.getOperatingPath(parameters);
            FakeFile f = fs.pathToFakeFile(currentDir);
            if (f.exists() && f.isDirectory()) {
                operatingSystem.fileSystem.FileSytemSimulator.currentDir = f.getPathField();
            } else {
                result = parameters + " Diretório não existe";
            }
        }
        //fim da implementacao do aluno
        return result;
    }

    public String rmdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
        System.out.println("\tParametros: " + parameters);

        String dir = Helper.getOperatingPath(parameters);
        FakeFile f = fs.pathToFakeFile(dir);
        if (f.exists() && f.isDirectory()) {
            if (f.listFiles().length == 0) {
                f.delete();
            } else {
                result = "rmdir: Diretório " + parameters + " possui arquivos e/ou diretorios. (Nada foi removido)";
            }
        } else {
            result = "rmdir: Diretório " + parameters + " não existe";
        }
        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String cp(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cp");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        final String[] psplit = parameters.split(" ");
        String dir_origin = null, opt = null, dir_dest = null;
        if (psplit.length > 2) {
            dir_origin = psplit[1];
            dir_dest = psplit[2];
            opt = psplit[0];
        } else {
            dir_origin = psplit[0];
            dir_dest = psplit[1];
        }
        FakeFile origin = fs.pathToFakeFile(Helper.getOperatingPath(dir_origin));
        FakeFile dest = fs.pathToFakeFile(Helper.getOperatingPath(dir_dest));
        if (origin.exists()) {
            if (origin.isFile()) {
                try {
                    if (dest.exists() && dest.isDirectory()) {
                        Fake.FileUtils.copyFileToDirectory(origin, dest);
                    } else {
                        FileUtils.copyFile(origin, dest);
                    }

                } catch (Exception e) {
                    result = "Erro " + e.getMessage();
                }
            } else {//É diretório
                if (opt != null && opt.contains("-R")) {
                    if (dest.exists()) {
                        if (dest.isDirectory()) {
                            try {
                                FileUtils.copyDirectoryToDirectory(origin, dest);
                            } catch (Exception e) {
                                result = "Erro " + e.getMessage();
                            }
                        } else {
                            result = "cp: o destino não é um diretório";
                        }
                    } else {
                        result = "cp: O diretorio de destino não existe. (Nada foi copiado)";
                    }
                } else {
                    result = "cp: A origem é um diretório";
                }
            }
        } else {
            result = "cp: O diretorio de origem não existe. (Nada foi copiado)";
        }
        //fim da implementacao do aluno
        return result;
    }

    public String mv(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: mv");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        final String[] psplit = parameters.split(" ");
        String dir_origin = null, opt = null, dir_dest = null;
        if (psplit.length == 2) {
            dir_origin = psplit[0];
            dir_dest = psplit[1];
            FakeFile origin = fs.pathToFakeFile(Helper.getOperatingPath(dir_origin));
            FakeFile dest = fs.pathToFakeFile(Helper.getOperatingPath(dir_dest));
            if (origin.exists()) {
                if (origin.isFile()) {
                    try {
                        if (dest.isDirectory()) {
                            FileUtils.moveFileToDirectory(origin, dest, false);
                        } else {
                            FileUtils.moveFile(origin, dest);
                        }

                    } catch (Exception e) {
                        result = "Erro " + e.getMessage();
                    }
                } else {//É diretório
                    if (dest.exists()) {
                        if (dest.isDirectory()) {
                            try {
                                FileUtils.moveDirectoryToDirectory(origin, dest, false);
                            } catch (Exception e) {
                                result = "Erro " + e.getMessage();
                            }
                        } else {
                            result = "mv: o destino não é um diretório";
                        }
                    } else {
                        result = "mv: Diretorio destino nao existe (Nenhuma alteracao foi efetuada)";
                    }
                }
            } else {
                result = "mv: Arquivo origem nao existe (Nenhuma alteracao foi efetuada)";
            }
        } else {
            result = "Parâmetros inválidos";
        }
        //fim da implementacao do aluno
        return result;
    }

    public String rm(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rm");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        final String[] psplit = parameters.split(" ");
        String dir = null, opt = null;
        if (psplit.length > 1) {
            dir = psplit[1];
            opt = psplit[0];
        } else {
            dir = psplit[0];
        }
        FakeFile f = fs.pathToFakeFile(Helper.getOperatingPath(dir));
        if (f.exists()) {
            if (f.isFile()) {
                f.delete();
            } else {
                if (opt != null && opt.contains("-R")) {
                    File[] fileswithin = f.listFiles();
                    for (File filewithin : fileswithin) {
                        filewithin.delete();
                    }
                    f.delete();
                } else {
                    result = "rm: " + dir + ": é um diretorio (Nenhum arquivo ou diretório foi removido)";
                }
            }
        } else {
            result = "rm: Arquivo nao existe (Nenhum arquivo ou diretório foi removido)";
        }
        //fim da implementacao do aluno

        return result;
    }

    public String chmod(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: chmod");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        ArrayList<String> psplit = new ArrayList<String>(Arrays.asList(parameters.split(" ")));
        String target = null, opt = null;
        int chmod;
        if (psplit.size() >= 2) {
            for (String psplit1 : psplit) {
                if (psplit1.substring(0, 1).equals("-")) {
                    opt = psplit1;
                    psplit.remove(psplit1);
                }
            }
            chmod = Integer.parseInt(psplit.get(0));
            target = psplit.get(1);
            FakeFile f = fs.pathToFakeFile(Helper.getOperatingPath(target));
            if (f.exists()) {
                if(Helper.validIntPosixPerm(chmod)){                    
                    f.chmod = chmod;
                }else{
                    result = "chmod: modo inválido: \""+chmod+"\"";
                }
            } else {
                result = "chmod: O arquivo " + f.getName() + " não existe";
            }

        } else {
            result = "chmod: Parâmetros inválidos";
        }

        //fim da implementacao do aluno
        return result;

    }

    public String createfile(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: createfile");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        ArrayList<String> psplit = new ArrayList<String>(Arrays.asList(parameters.split(" ", 2)));
        FakeFile f = fs.pathToFakeFile(Helper.getOperatingPath(psplit.get(0)));
        if (!f.exists()) {
            try {
                f.createNewFile();                
                if (psplit.size() > 1) {
                    String a = StringEscapeUtils.unescapeJava(psplit.get(1));
                    f.data = a.getBytes(Charset.forName("UTF-8"));
                }else{
                    f.data = "".getBytes(Charset.forName("UTF-8"));
                }
            } catch (FileNotFoundException e) {
                result += "createfile: " + e.getMessage();
            } catch (IOException e) {
                result += "createfile: " + e.getMessage();
            }
        } else {
            result += "createfile: Arquivo já existe. Não foi possível cria-lo";
        }
        //fim da implementacao do aluno
        return result;
    }

    public String cat(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cat");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        FakeFile f = fs.pathToFakeFile(Helper.getOperatingPath(parameters));
        if (f.exists()) {         
            if(f.isFile()){
            try {                
                byte[] data = f.data;
                result += new String(data, "UTF-8");
            } catch (IOException ex) {
                result += "cat: Arquivo não existe.";
            }
            }else{
                result +="cat: " + f.getName() + ": é um diretório";
            }
        } else {
            result += "cat: Arquivo não existe.";
        }
        //fim da implementacao do aluno
        return result;
    }

    public String batch(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: batch");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        File f = new File(parameters);
        if (f.exists()) {
            Scanner s;
            try {
                s = new Scanner(f);
                try {
                    while (s.hasNextLine()) {
                        ArrayList<String> cmd = new ArrayList(Arrays.asList(s.nextLine().split(" ", 2)));
                        java.lang.reflect.Method method;
                        method = this.getClass().getMethod(cmd.get(0), String.class);
                        method.invoke(this, cmd.get(1));
                    }
                    result += "Comandos executados";
                } catch (SecurityException e) {
                    // deu merda
                } catch (IllegalArgumentException e) {
                    // deu merda 2
                } catch (IllegalAccessException e) {
                    // silence is gold
                } catch (InvocationTargetException e) {
                    // deu merga 4
                }
            } catch (FileNotFoundException ex) {
                result += "batch: Arquivo não existe.";
            } catch (NoSuchMethodException e) {
                result += "batch: Há algum comando inválido";
            }
        } else {
            result += "batch: Arquivo não existe.";
        }
        //fim da implementacao do aluno
        return result;
    }

    public String info() {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: info");
        System.out.println("\tParametros: sem parametros");

        //nome do aluno
        String name = "Adriel Cardoso dos Santos";
        //numero de matricula
        String registration = "2015.1.08.001";
        //versao do sistema de arquivos
        String version = "0.1";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }

    public String dump(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: dump");
        System.out.println("\tParametros: " + parameters);

        File f = new File(parameters);
        if (!f.exists()) {
            ArrayList<String> cmds = Helper.getCommandsDump(fs.pathToFakeFile(Helper.getOperatingPath("./")), "./");

            try {
                f.createNewFile();
                PrintWriter writer = new PrintWriter(f.getPath(), "UTF-8");
                String output = "";
                for (String cmd : cmds) {
                    output += cmd + "\n";
                }
                writer.print(output);
                writer.close();
                cmds.clear();
            } catch (Exception e) {
                result = "dump: " + e.getMessage();
            }
        } else {
            result += "dump: O arquivo " + f.getName() + " já existe";
        }

        return result;
    }

}
