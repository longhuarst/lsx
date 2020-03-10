package com.lsx.service.mapper;

public interface PhotoWallMapper {

    void updatePhotoWall(String username, String photoList);


    String findByUsername(String username);
}
