/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service;

import com.bubble.pilipili.common.util.StringUtil;
import com.bubble.pilipili.oss.util.OssFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * 自定义临时文件MultipartFile对象
 * @author Bubble
 * @date 2025.03.26 20:48
 */
@Slf4j
public class TempMultipartFile implements MultipartFile {

    private final File file;
    private final String contentType;
    private final String originalFilename;

    private byte[] bytes;

    public TempMultipartFile(File file, String contentType, String originalFilename) {
        this.file = file;
        this.contentType = contentType;
        this.originalFilename = originalFilename;
    }

    /**
     * 创建一份上传文件的临时文件
     * <br>由于默认MultipartFile的上传临时文件会在请求结束后被清除，为了异步访问需要另存临时文件
     * @param multipartFile
     * @return
     */
    public static TempMultipartFile createTempFile(MultipartFile multipartFile) {
        String tmpFileName = UUID.randomUUID() + ".tmp";
        String contentType = multipartFile.getContentType();
        // 转移临时文件，用于异步访问
        File tmpFile = new File(OssFileUtil.TMP_DIR, tmpFileName);
        try {
            multipartFile.transferTo(tmpFile);
        } catch (IOException e) {
            log.error("临时文件转移失败");
            throw new RuntimeException(e);
        }
        return new TempMultipartFile(tmpFile, contentType, tmpFileName);
    }

    /**
     * 删除该临时文件
     */
    public void delete() {
        if (!this.file.delete()) {
            log.error("临时文件[{}]删除失败", this.file.getName());
        }
    }

    /**
     * 获取文件全路径
     * @return
     */
    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }

    /**
     * Return the name of the parameter in the multipart form.
     *
     * @return the name of the parameter (never {@code null} or empty)
     */
    @NotNull
    @Override
    public String getName() {
        return file.getName();
    }

    /**
     * Return the original filename in the client's filesystem.
     * <p>This may contain path information depending on the browser used,
     * but it typically will not with any other than Opera.
     * <p><strong>Note:</strong> Please keep in mind this filename is supplied
     * by the client and should not be used blindly. In addition to not using
     * the directory portion, the file name could also contain characters such
     * as ".." and others that can be used maliciously. It is recommended to not
     * use this filename directly. Preferably generate a unique one and save
     * this one somewhere for reference, if necessary.
     *
     * @return the original filename, or the empty String if no file has been chosen
     * in the multipart form, or {@code null} if not defined or not available
     * @see FileItem#getName()
     * @see CommonsMultipartFile#setPreserveFilename
     * @see <a href="https://tools.ietf.org/html/rfc7578#section-4.2">RFC 7578, Section 4.2</a>
     * @see <a href="https://owasp.org/www-community/vulnerabilities/Unrestricted_File_Upload">Unrestricted File Upload</a>
     */
    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    /**
     * Return the content type of the file.
     *
     * @return the content type, or {@code null} if not defined
     * (or no file has been chosen in the multipart form)
     */
    @Override
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Return whether the uploaded file is empty, that is, either no file has
     * been chosen in the multipart form or the chosen file has no content.
     */
    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    /**
     * Return the size of the file in bytes.
     *
     * @return the size of the file, or 0 if empty
     */
    @Override
    public long getSize() {
        return file.length();
    }

    /**
     * Return the contents of the file as an array of bytes.
     *
     * @return the contents of the file as bytes, or an empty byte array if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        if (bytes != null && bytes.length > 0) {
            return bytes;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            IOUtils.readFully(fis, bytes);
        }
        return bytes;
    }

    /**
     * Return an InputStream to read the contents of the file from.
     * <p>The user is responsible for closing the returned stream.
     *
     * @return the contents of the file as stream, or an empty stream if empty
     * @throws IOException in case of access errors (if the temporary store fails)
     */
    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }

    /**
     * Return a Resource representation of this MultipartFile. This can be used
     * as input to the {@code RestTemplate} or the {@code WebClient} to expose
     * content length and the filename along with the InputStream.
     *
     * @return this MultipartFile adapted to the Resource contract
     * @since 5.1
     */
    @NotNull
    @Override
    public Resource getResource() {
        return MultipartFile.super.getResource();
    }

    /**
     * Transfer the received file to the given destination file.
     * <p>This may either move the file in the filesystem, copy the file in the
     * filesystem, or save memory-held contents to the destination file. If the
     * destination file already exists, it will be deleted first.
     * <p>If the target file has been moved in the filesystem, this operation
     * cannot be invoked again afterwards. Therefore, call this method just once
     * in order to work with any storage mechanism.
     * <p><b>NOTE:</b> Depending on the underlying provider, temporary storage
     * may be container-dependent, including the base directory for relative
     * destinations specified here (e.g. with Servlet 3.0 multipart handling).
     * For absolute destinations, the target file may get renamed/moved from its
     * temporary location or newly copied, even if a temporary copy already exists.
     *
     * @param dest the destination file (typically absolute)
     * @throws IOException           in case of reading or writing errors
     * @throws IllegalStateException if the file has already been moved
     *                               in the filesystem and is not available anymore for another transfer
     * @see FileItem#write(File)
     * @see Part#write(String)
     */
    @Override
    public void transferTo(@NotNull File dest) throws IOException, IllegalStateException {
        try (OutputStream outputStream = Files.newOutputStream(dest.toPath())) {
            outputStream.write(getBytes());
        }
    }

    /**
     * Transfer the received file to the given destination file.
     * <p>The default implementation simply copies the file input stream.
     *
     * @param dest
     * @see #getInputStream()
     * @see #transferTo(File)
     * @since 5.1
     */
    @Override
    public void transferTo(@NotNull Path dest) throws IOException, IllegalStateException {
        try (OutputStream outputStream = Files.newOutputStream(dest)) {
            outputStream.write(getBytes());
        }
    }
}
