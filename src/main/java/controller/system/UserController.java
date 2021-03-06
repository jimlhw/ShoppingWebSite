package controller.system;

import common.constants.CookieConstants;
import common.dto.User.UserCheckDTO;
import common.dto.User.UserLoginDTO;
import common.dto.User.UserRegisterDTO;
import common.entity.system.User;
import common.enums.RegisterEnum;
import common.enums.UserSellerStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.system.IUserService;
import utils.PrimaryKeyGenerator;
import utils.TokenGenerator;

/**
 * Created by peyppicp on 2017/3/21.
 */
@Controller
public class UserController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "/userCheck.action", method = RequestMethod.POST)
    @ResponseBody
    public UserCheckDTO executeUserCheck(@RequestParam String userAccount) {
        User user = new User();
        user.setUser_account(userAccount);
        User entity = iUserService.getEntity(user);
        if (entity == null) {
            UserCheckDTO userCheckDTO = new UserCheckDTO();
            userCheckDTO.setDetailed_message("用户不存在！");
            userCheckDTO.setMessage("error");
            userCheckDTO.setStatus(1);
            return userCheckDTO;
        }
        UserCheckDTO userCheckDTO = new UserCheckDTO();
        userCheckDTO.setMessage("success");
        userCheckDTO.setDetailed_message("用户已存在！");
        userCheckDTO.setStatus(0);
        return userCheckDTO;
    }

    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    @ResponseBody
    public UserLoginDTO executeLogin(@RequestParam String userAccount, @RequestParam String userPassword) {

        User user = new User();
        user.setUser_account(userAccount);
        User login = iUserService.getEntity(user);
        if (login.getUser_password().equals(userPassword)) {
            String token = TokenGenerator.token(userAccount, userPassword, login.getUser_id());
//            set the token into cache
//            EasyCache.getInstance().setValue(token, login.getUser_id());
            UserLoginDTO userLoginDTO = new UserLoginDTO();
            userLoginDTO.setToken(token);
            userLoginDTO.setUser_nickname(login.getUser_nickname());
            userLoginDTO.setMessage("success");
            userLoginDTO.setError_message("ok");
            userLoginDTO.setCookie_time(CookieConstants.TIME);
            userLoginDTO.setStatus(0);
            return userLoginDTO;
        }
        return null;
    }

    @RequestMapping(value = "/register.action", method = RequestMethod.POST)
    @ResponseBody
    public UserRegisterDTO executeRegister(@RequestParam String userAccount, @RequestParam String userPassword,
                                           @RequestParam String userNickName, @RequestParam String userRealName,
                                           @RequestParam String userPhoneNum, @RequestParam String userEmail) {
        User user = new User();
        user.setUser_id(PrimaryKeyGenerator.uuid());
        user.setUser_account(userAccount);
        user.setUser_password(userPassword);
        user.setUser_nickname(userNickName);
        user.setUser_realname(userRealName);
        user.setUser_phonenum(userPhoneNum);
        user.setUser_email(userEmail);
        user.setUser_credit(0);
        user.setUser_points(0);
        user.setUser_seller_status(UserSellerStatusEnum.FALSE.getStatus());
        //TODO
        user.setUser_key("");
        RegisterEnum register = iUserService.register(user);
        if (register.getCode() == RegisterEnum.SUCCESS.getCode()) {
            String token = TokenGenerator.token(userAccount, userPassword, user.getUser_id());
//            EasyCache.getInstance().setValue(token, userAccount);
            UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
            userRegisterDTO.setUser_nickname(userNickName);
            userRegisterDTO.setError_message("ok");
            userRegisterDTO.setStatus(0);
            userRegisterDTO.setMessage("success");
            userRegisterDTO.setToken(token);
            userRegisterDTO.setCookie_time(10000000);
            return userRegisterDTO;
        } else if (register.getCode() == RegisterEnum.UNKNOWEN.getCode()) {
            UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
            userRegisterDTO.setMessage("error");
            userRegisterDTO.setError_message("未知错误");
            userRegisterDTO.setStatus(1);
            return userRegisterDTO;
        }
        return null;
    }
}
