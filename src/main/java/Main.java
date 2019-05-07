
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {
        port(8080);

        AccountHandler handler = new AccountHandler();

        get("/alive", (request,response) -> "Alive!");

        post("/account", (request, response) -> {
            System.out.println("received request " + request.body());
            int accountID = handler.createAccount(request.body());
            response.status(201);
            return "Account ID: " + accountID;
        });

        get("/account/balance", (request, response) -> {
            double balance = 0;
            System.out.println("GET /account/balance: queryParams " + request.queryParams());
            try {
                int accountID = Integer.parseInt(request.queryParams("id"));
                balance = handler.getBalance(accountID);
                response.status(200);
            }
            catch (NumberFormatException e) {
                response.status(400);
            }
            catch (UnknownAccountException e) {
                return e.getMessage();
            }
            return "Account Balance: " + balance;
        });

        post("/account/balance", (request, response) -> {
            System.out.println("POST /account/balance: queryParams" + request.queryParams());
            int id = Integer.parseInt(request.queryParams("id"));
            double value = Double.parseDouble(request.queryParams("deposit"));
            try {
                return "New Balance: " + handler.updateBalance(id, value);
            }
            catch (OverdrawException e) {
                return e.getMessage();
            }
        });

        post("/account/transfer", (request, response) -> {
           int idSend = Integer.parseInt(request.queryParams("idSend"));
           int idReceive = Integer.parseInt(request.queryParams("idReceive"));
           double funds = Double.parseDouble(request.queryParams("funds"));
           try {
               handler.transferFunds(idSend, idReceive, funds);
               return String.format("Transfered %.2f from %d to %d", funds, idSend, idReceive);
           }
           catch (OverdrawException e) {
               return e.getMessage();
           }
        });

    }
}
