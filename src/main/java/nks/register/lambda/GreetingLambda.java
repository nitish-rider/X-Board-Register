package nks.register.lambda;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import nks.register.lambda.model.User;

public class GreetingLambda implements RequestHandler<User, String> {

    @Override
    public String handleRequest(User input, Context context) {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-south-1")
                .build();

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        System.out.println(input.getName());
        System.out.println(input.getEmail());
        System.out.println(input.getPassword());

        return checkData(mapper,input);
    }

    private String checkData(DynamoDBMapper mapper, User input) {
        UserData userData=new UserData();
        userData.setName(input.getName());

        UserData result=mapper.load(userData);
        if(!(result==null)){
            System.out.println(result.getName());
            System.out.println(result.getEmail());
            System.out.println(result.getUser());
            if(result.getEmail().equals(input.getEmail())){
               return "Email Id Already Exist";
           }
           else{
               return "UserName Not Available";
           }
        }
        else{
            saveData(mapper,input);
            return "Success";
        }
    }

    private static void saveData(DynamoDBMapper mapper,User input){
        UserData data=new UserData();
        data.setName(input.getName());
        data.setEmail(input.getEmail());
        String pass= input.getName()+"##"+input.getPassword();
        data.setUser(pass);
        mapper.save(data);
    }
}
