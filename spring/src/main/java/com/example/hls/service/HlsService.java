package com.example.hls.service;

import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HlsService {
    private final Environment env;
    public Resource getVideoRes(String tsFile, String streamKey) throws IOException {
        File file = new File(getConvertVideoPath(tsFile, streamKey));
        return new FileSystemResource(file.getCanonicalPath());
    }

    private String getRawVideoPath(String videoFileName, String streamKey) {
        return "/usr/share/nginx/temp/hls/" + streamKey + "/" + videoFileName;
    }

    private String getRawVideoPath(String videoFileName) {
        return "../resource/file" + videoFileName;
    }

    private String getConvertVideoPath(String outputFileName, String streamKey) {
        return "/usr/share/nginx/temp/hls/" + streamKey + "/" + outputFileName;
    }

    private String getConvertVideoPath(String outputFileName) {
        return "../resource/convert" + outputFileName;
    }

    private String getFfmpegPath() throws IOException {
        String returnValue = "";

        if(env.getProperty("operation.system").equals("windows")) {
            returnValue = new File("src/main/resources/ffmpeg/windows/ffmpeg-7.1/bin/ffmpeg.exe").getCanonicalPath();
        } else {
            returnValue = "usr/bin/ffmpeg";
        }

        return returnValue;
    }

    private String getFfprobePath() throws IOException {
        String returnValue = "";

        if(env.getProperty("operation.system").equals("windows")) {
            returnValue = new File("src/main/resources/ffmpeg/windows/ffmpeg-7.1/bin/ffprobe.exe").getCanonicalPath();
        } else {
            returnValue = "usr/bin/ffprobe";
        }

        return returnValue;
    }

    public void convertHls() throws IOException {
        String videoFilePath = new File(getRawVideoPath("/alpha_.mp4")).getCanonicalPath();
        String convertPath =  new File(getConvertVideoPath("/playlist.m3u8")).getCanonicalPath();

        String ffprobePath = getFfprobePath();
        String ffmpegPath = getFfmpegPath();


        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoFilePath)
                .addOutput(convertPath)
                .addExtraArgs("-profile:v", "baseline")
                .addExtraArgs("-level", "3.0")
                .addExtraArgs("-start_number", "0")
                .addExtraArgs("-hls_time", "10")
                .addExtraArgs("-hls_list_size", "0")
                .addExtraArgs("-f", "hls")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(new FFmpeg(ffmpegPath), new FFprobe(ffprobePath));
        executor.createJob(builder).run();
    }
}
