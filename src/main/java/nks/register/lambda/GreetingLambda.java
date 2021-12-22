package nks.register.lambda;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import nks.register.lambda.model.User;

import java.util.regex.Pattern;

public class GreetingLambda implements RequestHandler<User, String> {

    @Override
    public String handleRequest(User input, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-south-1")
                .build();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        return checkData(mapper, input);
    }

    private String checkData(DynamoDBMapper mapper, User input) {
        UserData userData = new UserData();
        userData.setName(input.getName());
        UserData result = mapper.load(userData);
        if (!(result == null)) {
            if (result.getEmail().equals(input.getEmail())) {
                return "Email Id Already Exist";
            } else {
                return "UserName Not Available";
            }
        } else {
            String response = saveData(mapper, input);
            return response;
        }
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private static String saveData(DynamoDBMapper mapper, User input) {
        UserData data = new UserData();
        if (!(input.getName() == "")) {
            data.setName(input.getName());
        }else {
            return "Invalid Name";
        }
        if (isEmailValid(input.getEmail())){
            data.setEmail(input.getEmail());
        }else {
            return "Invalid Email";
        }
        String pass = input.getName() + "##" + input.getPassword();
        data.setUser(pass);
        mapper.save(data);

        return "Success";
    }
}
