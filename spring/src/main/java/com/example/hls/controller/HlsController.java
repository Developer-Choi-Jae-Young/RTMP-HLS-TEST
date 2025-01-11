package com.example.hls.controller;

import com.example.hls.service.HlsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HlsController {
    private final HlsService hlsService;

    @GetMapping("/{streamKey}/{hlsName}.m3u8")
    public ResponseEntity<Resource> getHls(@PathVariable String streamKey, @PathVariable String hlsName) throws IOException {
        Resource resource = hlsService.getVideoRes(hlsName + ".m3u8", streamKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=playlist.m3u8");
        headers.setContentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/{streamKey}/{tsName}.ts")
    public ResponseEntity<Resource> getHlsTs(@PathVariable String streamKey, @PathVariable String tsName) throws IOException {
        tsName = tsName + ".ts";
        Resource resource = hlsService.getVideoRes(tsName, streamKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tsName);
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/file/{streamKey}/{fileName}")
    public ResponseEntity<Resource> getHlsFile(@PathVariable String streamKey, @PathVariable String fileName) throws IOException {
        Resource resource = hlsService.getVideoRes(fileName, streamKey);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convert() throws IOException {
        hlsService.convertHls();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publish(@RequestParam("name") String streamKey) {
        log.info("publish");
        log.info(streamKey);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/done")
    public ResponseEntity<?> done(@RequestParam("name") String streamKey) {
        log.info("done");
        log.info(streamKey);
        return ResponseEntity.ok().build();
    }
}
