package pvr3.tfg.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Pablo on 15/07/2015.
 */
@RestController
public class RestUtilsController {

    @RequestMapping(value = "/get-headers", method = RequestMethod.POST)
    public List<String> getHeadersOfFile(@RequestParam("file") MultipartFile file){
        ArrayList<String> headers = new ArrayList<>();
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();
                InputStream is = new ByteArrayInputStream(bytes);
                Scanner sc = new Scanner(is);
                String line = sc.nextLine();
                sc.close();
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                while(st.hasMoreTokens()){
                    String token = st.nextToken();
                    if(!token.equals("NONE"))
                        headers.add(token);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return headers;
    }
}
