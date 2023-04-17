package com.hj.serivce;

import com.hj.domain.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    ResponseResult uploadImg(MultipartFile img);
}
