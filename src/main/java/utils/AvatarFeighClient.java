package utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "avatar", url = "https://some-random-api.ml/img/dog")
public interface AvatarFeighClient {

    @RequestMapping(method = RequestMethod.GET, value = "")
    Avatar getAvatar();
}
