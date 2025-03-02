import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;

 class FileManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static String userDir=System.getProperty("user.dir");
    private static File currentDirectory=(userDir!=null)? new File(userDir):new File("C:\\");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n" + currentDirectory.getAbsolutePath() + " >");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";


            switch (command) {
                case "creator-folder":
                    createFolder(argument);
                    break;
                case "creator-file":
                    createFile(argument);
                    break;
                case "delete":
                    delete(argument, false);
                    break;
                case "delete-r":
                    delete(argument, true);
                    break;
                case "rename":
                    rename(argument);
                    break;
                case "move":
                    move(argument);
                    break;
                case "list":
                    listFiles(argument);
                    break;
                case "size":
                    calculateSize(argument);
                    break;
                case "cd":
                    changeDirectory(argument);
                    break;
                case "disk":
                    changeDisk(argument);
                    break;
                case "exit":
                    System.out.println("Выход из программы ...");
                    return;
                default:
                    System.out.println("Неизвестная команда. Введите 'help' для списка команд. ");

            }

        }


    }

    private static void createFolder(String folderName) {
        if (folderName.isEmpty()) {
            System.out.println("Укажите имя папки. ");
            return;
        }
        File folder = new File(currentDirectory, folderName);
        if (folder.mkdir()) {
            System.out.println("Папка создана: " + folder.getAbsolutePath());
        } else {
            System.out.println("Ошибка при создании папки. ");
        }
    }

    public static void createFile(String fileName) {
        if (fileName.isEmpty()) {
            System.out.println("Укажите имя файла");
            return;
        }
        File file = new File(currentDirectory, fileName);
        try {
            if (file.createNewFile()) {
                System.out.println("Файл создан ." + file.getAbsolutePath());
            } else {
                System.out.println("Файл уже существует .");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла ." + e.getMessage());
        }
    }

    public static void delete(String path, boolean recursive) {
        File file = new File(currentDirectory, path);
        if (!file.exists()) {
            System.out.println("Файл или папка не найдены .");
            return;
        }
        System.out.println("Вы уверены, что хотите удалить  ' " + file.getName() + "'? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (!confirmation.equals("y")) {
            System.out.println("Отмена удаления .");
            return;
        }
        if (file.isDirectory() && recursive) {
            deleteDirectory(file);
            System.out.println("Папка удалена" + file.getAbsolutePath());
        } else if (file.delete()) {
            System.out.println("Удалено: " + file.getAbsolutePath());

        } else {
            System.out.println("Ошибка при удалении .");
        }
    }

    public static void deleteDirectory(File dir) {
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File file : contents) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }

    public static void rename(String args) {
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Укажите текущее иновое имя .");
            return;
        }
        File oldFile = new File(currentDirectory, parts[0]);
        File newFile = new File(currentDirectory, parts[1]);
        if (oldFile.renameTo(newFile)) {
            System.out.println("Переименовано:" + oldFile.getName() + " ->" + newFile.getName());
        } else {
            System.out.println("Ошибка при переименовании .");
        }
    }

    public static void move(String args) {
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Укажите исходный путь и новый путь");
            return;
        }
        File file = new File(currentDirectory, parts[0]);
        File destination = new File(parts[1]);
        if (file.renameTo(new File(destination, file.getName()))) {
            System.out.println("Файл перемещен в :" + destination);

        } else {
            System.out.println("Ошибка при перемещении.");
        }

    }

    public static void listFiles(String option) {
        File[] files = currentDirectory.listFiles();
        if (files == null) {
            System.out.println("Ошибка при чтении содержимого папки");
            return;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));
        if (option.equals("size")) {
            Arrays.sort(files, Comparator.comparingLong(File::length));
        }
        for (File file : files) {
            System.out.printf("%s %s (%d байт)%n",
                    file.isDirectory() ? "[DIR]" : "[FILE]",
                    file.getName(),
                    file.length());
        }
    }

    public static void calculateSize(String path) {
        File file = new File(currentDirectory, path);
        if (!file.exists()) {
            System.out.println("Файл или папка не найдены");
            return;
        }
        long size = file.isDirectory() ? getFolderSize(file) : file.length();
        System.out.println("Paзмер " + size + "байт");
    }

    private static long getFolderSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        if (files!=null){
            for (File file : files){
            size += file.isDirectory() ? getFolderSize(file) : file.length();
        }
    }
    return size;
}
private static void changeDirectory(String path){
    File newDir=new File(currentDirectory,path);
    if (newDir.exists()&&newDir.isDirectory()){
        currentDirectory=newDir;
        System.out.println("Перешли в папку:"+ currentDirectory.getAbsolutePath());
    }else{
        System.out.println("Ошибка. Паппка не найдена.");
    }
    }
    private static void changeDisk(String driveLetter){
        File newDisk=new File(driveLetter+" :\\");
        if (newDisk.exists()&& newDisk.isDirectory()){
            currentDirectory=newDisk;
            System.out.println("Диск сменен на :"+currentDirectory.getAbsolutePath());
        }else {
            System.out.println("Ошибка. Диск не найден");
        }
    }
}
