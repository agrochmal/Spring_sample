package pl.demo.core.service;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Created by robertsikora on 29.07.15.
 */
@Validated
public interface ResourceMediaService {
    Long upload(@NotNull byte[] image);
}
