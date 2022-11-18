package sat.recruitment.api.service;

import org.apache.catalina.Manager;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sat.recruitment.api.model.User;
import sat.recruitment.api.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static Logger logger = (Logger) LogManager.getLogger(UserService.class);
    private List<User> users = new ArrayList<User>();

    public void saveOrUpdate(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setAddress(user.getAddress());
        newUser.setPhone(user.getPhone());
        newUser.setUserType(user.getUserType());
        newUser.setMoney(user.getMoney());

        normalUser(newUser);
        superUser(newUser);
        premiumUser(newUser);

        saveFile();
        verifyDuplicate(newUser);

        userRepository.save(user);
    }

    private void verifyDuplicate(User newUser) {
        Boolean isDuplicated = false;
        for (User users : users) {

            if (users.getEmail().equals(newUser.getEmail()) || users.getPhone().equals(newUser.getPhone())) {
                isDuplicated = true;
            } else if (users.getName().equals(newUser.getName())) {
                if (users.getAddress().equals(newUser.getAddress())) {
                    isDuplicated = true;
                }

            }
        }
        if (isDuplicated) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is duplicated");
        }
    }


    public void saveFile(){
        InputStream fstream;
        try {
            fstream = getClass().getResourceAsStream("/users.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            while ((strLine = br.readLine()) != null) {
                String[] line = strLine.split(",");
                User user = new User();
                user.setName(line[0]);
                user.setEmail(line[1]);
                user.setPhone(line[2]);
                user.setAddress(line[3]);
                user.setUserType(line[4]);
                user.setMoney(Double.valueOf(line[5]));
                users.add(user);

            }
            fstream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("problemas en salvar archivo", e.getMessage());
        }
    }

    public void normalUser(User newUser) {
        if (newUser.getUserType().equals("Normal")) {
            if (Double.valueOf(newUser.getMoney()) > 100) {
                Double percentage = Double.valueOf("0.12");
                // If new user is normal and has more than USD100
                var gif = Double.valueOf(newUser.getMoney()) * percentage;
                newUser.setMoney(newUser.getMoney() + gif);
            }
            if (Double.valueOf(newUser.getMoney()) < 100) {
                if (Double.valueOf(newUser.getMoney()) > 10) {
                    var percentage = Double.valueOf("0.8");
                    var gif = Double.valueOf(newUser.getMoney()) * percentage;
                    newUser.setMoney(newUser.getMoney() + gif);
                }
            }
        }
    }
    public void superUser(User newUser) {
        if (newUser.getUserType().equals("SuperUser")) {
            if (Double.valueOf(newUser.getMoney()) > 100) {
                Double percentage = Double.valueOf("0.20");
                Double gif = Double.valueOf(newUser.getMoney()) * percentage;
                newUser.setMoney(newUser.getMoney() + gif);
            }
        }
    }

    public void premiumUser(User newUser){
        if (newUser.getUserType().equals("Premium")) {
            if (Double.valueOf(newUser.getMoney()) > 100) {
                Double gif = Double.valueOf(newUser.getMoney()) * 2;
                newUser.setMoney(newUser.getMoney() + gif);
            }
        }
    }
}
