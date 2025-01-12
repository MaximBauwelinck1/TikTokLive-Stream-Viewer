package DTO;

import java.awt.Image;

import io.github.jwdeveloper.tiktok.data.models.Picture;

public record DonatieDTO(
    String nameGift,
    Image giftPicture,
    String fromUser,
    Image userPicture
) {}