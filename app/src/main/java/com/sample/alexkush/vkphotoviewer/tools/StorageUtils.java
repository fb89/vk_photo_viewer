package com.sample.alexkush.vkphotoviewer.tools;

import android.os.Environment;

import java.io.File;

/**
 * Класс с методами для доступа к файловой системе
 */
public class StorageUtils {
    public static final String SHARE_FOLDER = "vk_viewer";

    /* Проверка возможности записи на SD-карту */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        return false;
    }

    /* Проверка возможности чтения с SD-карты */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return true;
        return false;
    }

    /**
     * Метод для создания файла в конкретной директории
     */
    public static File createFile(String folderPath, String fileName) throws FileAccessException {
        if (isExternalStorageWritable()) {
            File file = new File(folderPath, fileName);
            File folder = file.getParentFile();
            if (!folder.exists() && !folder.mkdirs())
                throw new FileAccessException("Ошибка создания директории");
            return file;
        }
        throw new FileAccessException("Нет доступа к карте памяти");
    }

    /**
     * Удаление папки со всем содержимым
     */
    public static void deleteFolder(File dir) {
        if (isExternalStorageWritable()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                if (children != null) {
                    for (int i = 0; i < children.length; i++)
                        new File(dir, children[i]).delete();
                }
            }
            dir.delete();
        }
    }

    public static class FileAccessException extends Exception {
        public FileAccessException(String detailMessage) {
            super(detailMessage);
        }
    }
}
