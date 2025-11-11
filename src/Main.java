import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.awt.Desktop;

public class Main {

    private static final String fileUrls = "urls.txt";
    private static final String musPath = "music/";

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(musPath));

            try (BufferedReader reader = new BufferedReader(new FileReader(fileUrls))) {
                String url;
                int count = 0;

                while ((url = reader.readLine()) != null) {
                    if (url.trim().isEmpty()) continue;

                    String savePath = musPath + "song" + (count + 1) + ".mp3";
                    System.out.println("Скачивание: " + url + "...");

                    try {
                        saveFromUrlwNIO(url, savePath);
                        System.out.println(" Успешно сохранено как: " + savePath);
                    } catch (IOException e) {
                        System.out.println(" Ошибка при загрузке: " + url + " — " + e.getMessage());
                    }

                    count++;
                }

                System.out.println("\nВсего обработано ссылок: " + count);

                playSystemPlayer(musPath);

            }

        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлами: " + e.getMessage());
        }
    }

    private static void saveFromUrlwNIO(String strUrl, String strfile) throws IOException {
        URL url = new URL(strUrl);
        java.net.URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/124.0 Safari/537.36");

        try (ReadableByteChannel byteChannel = Channels.newChannel(connection.getInputStream());
             FileOutputStream stream = new FileOutputStream(strfile)) {
            stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
        }
    }

    private static void playSystemPlayer(String directoryPath) {
        File dir = new File(directoryPath);
        File[] mp3Files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".mp3"));

        if (mp3Files == null || mp3Files.length == 0) {
            System.out.println("Нет MP3 файлов для воспроизведения.");
            return;
        }

        System.out.println("\nЗапуск системного плеера для проигрывания треков...");

        try {
            for (File file : mp3Files) {
                System.out.println("Играет: " + file.getName());
                Desktop.getDesktop().open(file);
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при открытии файлов: " + e.getMessage());
        }

        System.out.println("Все треки запущены в системном плеере.");
    }
}
