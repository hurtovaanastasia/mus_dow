import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;

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

                    try (ReadableByteChannel channel = Channels.newChannel(new URL(url).openStream());
                         FileOutputStream fos = new FileOutputStream(savePath)) {
                        fos.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                        System.out.println(" Успешно сохранено как: " + savePath);
                    } catch (IOException e) {
                        System.out.println(" Ошибка при загрузке: " + url + " — " + e.getMessage());
                    }

                    count++;
                }

                System.out.println("\nВсего обработано ссылок: " + count);
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
}

