import java.io.*;
import java.util.*;
class Bank implements Serializable {
    Scanner scanner = new Scanner(System.in);
    protected String accountHolderName;
    protected int age;
    protected String CNIC_No;
    protected int accountNumber = 0;
    protected double balance = 0;
    protected int atmCode;


    //---------Setters---------------------------------------------

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    //-------------------------Getters-------------------------------

    public String getAccountHolderName() {
        return accountHolderName;
    }
    public int getAge() {
        return age;
    }
    public double getBalance() {
        return balance;
    }

    //---------------------------------------------------------------

    public void CreateAccount() {
        System.out.printf("Enter your name: ");
        accountHolderName = scanner.next();
        accountNumber++;
        int ageFlag = 0;
        do {
            System.out.printf("Enter your age: ");
            age = scanner.nextInt();
            if (age > 0 && age < 18) {
                System.out.println("\nYou should be atleast 18 to create an account");
                break;
            }
            if (age <= 0) {
                System.out.println("\nYou cannot enter age 0 or negative");
            } else {
                int cnicFlag = 0;
                do {
                    System.out.printf("Enter your CNIC Number without dashes:");

                    CNIC_No = scanner.next();
                    if (CNIC_No.length() != 13) {
                        System.out.println("CNIC Number must have 13 digits");
                    } else {
                        cnicFlag = 1;
                    }
                } while (cnicFlag == 0);
                atmCode = Integer.parseInt(CNIC_No, 0, 4, 10);
                System.out.println("\nYour ATM Code is the first four digits of your CNIC Number");
                ageFlag = 1;
            }
        } while (ageFlag == 0);
        System.out.printf("\nEnter the balance you want to deposit in your account: ");
        int balance= scanner.nextInt();
        setBalance(balance);
        System.out.println("\nAccount has been created successfully.");
    }
}


class BankAccount extends Bank implements Serializable {
    public BankAccount() throws IOException {
        super();
    }
    public void filingWrite () throws IOException {
        File f = new File("Account_Details.txt");
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter( new FileWriter(f,true));
        bw.flush();
        bw.write(CNIC_No);
        bw.flush();
        bw.newLine();
        bw.write(String.valueOf(age) );
        bw.flush();
        bw.newLine();
        bw.write(accountHolderName);
        bw.flush();
        bw.newLine();
        bw.write(String.valueOf(accountNumber));
        bw.flush();
        bw.newLine();
        bw.write(String.valueOf(balance));
        bw.flush();
        bw.newLine();
        bw.write(String.valueOf(atmCode));
        bw.flush();
        bw.newLine();
        bw.close();
    }
    public void remove(String cnic) throws IOException {
        ArrayList<String> list =  new ArrayList<>();
        Boolean EOF = false;
        String temp;
        int index = 0;
        File f = new File("Account_Details.txt");
        FileReader istream = new FileReader(f);
        BufferedReader br = new BufferedReader(istream);
        while(!EOF){
            temp = br.readLine();
            if(temp != null){
                list.add(temp);
            }
            else{
                EOF=true;
            }
        }
        br.close();
        for(int i = 0 ; i<list.size() ;i++){
            if(list.get(i).equals(cnic)){
                index = i;
            }
        }
        list.remove(index+5);
        list.remove(index+4);
        list.remove(index+3);
        list.remove(index+2);
        list.remove(index+1);
        list.remove(index);

        BufferedWriter bw = new BufferedWriter( new FileWriter(f));
        for(int i=0;i<list.size();i++){
            bw.newLine();
            bw.flush();
            bw.write(list.get(i));
            bw.flush();
        }
        bw.newLine();
        bw.close();
        System.out.println("The account has been deleted successfully.");
    }
    public void update(String cnic,boolean updateATMpin, boolean updateBalance) throws IOException {
        ArrayList<String> list =  new ArrayList<>();
        Boolean EOF = false;
        String temp;
        int index = 0;
        File f = new File("Account_Details.txt");
        FileReader istream = new FileReader(f);
        BufferedReader br = new BufferedReader(istream);
        while(!EOF){
            temp = br.readLine();
            if(temp != null){
                list.add(temp);
            }
            else{
                EOF=true;
            }
        }
        br.close();
        for(int i = 0 ; i<list.size() ;i++){
            if(list.get(i).equals(cnic)){
                index = i;
            }
        }
        if (updateATMpin == true) {
            System.out.printf("\nEnter new ATM Code");
            list.set(index+5,scanner.next()); //atmCode
        }
        if (updateBalance==true) {
            System.out.printf("\nEnter new balance: ");
            list.set(index + 4, scanner.next()); //balance
        }
        BufferedWriter bw = new BufferedWriter( new FileWriter(f));
        for(int i=0;i<list.size();i++){
            bw.newLine();
            bw.flush();
            bw.write(list.get(i));
            bw.flush();
        }
        bw.newLine();
        bw.close();
    }
}

class ATM {

    Scanner scan1 = new Scanner(System.in);
    BankAccount bankAccount= new BankAccount();
    private String code;
    private int loginFlag = 0;
    private int index = 0;
    private double amount;
    private ArrayList<String> list =  new ArrayList<>();
    public ATM() throws IOException {
    }


    //----------------------------------------------------------------------------
    public void login() throws IOException {

        //----------------------------------------------------------------------------
        Boolean EOF = false;
        String temp;
        File f = new File("Account_Details.txt");
        FileReader istream = new FileReader(f);
        BufferedReader br = new BufferedReader(istream);
        while(!EOF){
            temp = br.readLine();
            if(temp != null){
                list.add(temp);
            }
            else{
                EOF=true;
            }
        }
        br.close();

        //----------------------------------------------------------------------------

        do {
            System.out.printf("\nEnter your CNIC: ");
            code = scan1.next();
            for(int i =0 ;i<list.size();i++){
                if(list.get(i).equals(code)){
                    index=i;
                    System.out.printf("Enter your pin code: ");
                    if(list.get(i+5).equals(scan1.next())){
                        loginFlag = 1;
                        System.out.println("Log in successful");
                        break;
                    }
                    else{
                        System.out.println("Incorrect password");
                        loginFlag = 0;
                        break;
                    }
                }
            }
        } while (loginFlag == 0);
    }

    //-----------------------------------------------------------------------------
    public void withDraw() throws IOException {
        double amount;
        int withdrawFlag = 0;
        if(loginFlag == 1){
            System.out.println("Enter the amount you want to withdraw:");
            amount = scan1.nextDouble();
            if (amount <= Double.parseDouble(list.get(index+4))) {
                amount = Double.parseDouble(list.get(index+4))- amount;
                list.set(index + 4, String.valueOf(amount));
                File f = new File("Account_Details.txt");
                BufferedWriter bw = new BufferedWriter( new FileWriter(f));
                for(int i=0;i<list.size();i++){
                    bw.newLine();
                    bw.flush();
                    bw.write(list.get(i));
                    bw.flush();
                }
                bw.newLine();
                bw.close();
                System.out.println("Amount withdrawed successfully.");
            }
            else if (amount > Double.parseDouble(list.get(index+4))){
                System.out.println("Not enough money in account");
            }
            if (amount < 0) {
                System.out.println("Withdraw amount cannot be negative!");
            }
            withdrawFlag = 0;
        }
        else{
            System.out.println("You failed to login and cannot withdraw cash");
        }
    }

    public void Deposit() throws IOException {
        double amount;
        int depositFlag = 0;
        if (loginFlag == 1) {

            System.out.printf("\nEnter the amount you want to deposit:");
            amount = scan1.nextDouble();

            if (amount < 0) {
                System.out.printf("Deposit amount cannot bo negative!");

            }
            else {
                amount = Double.parseDouble(list.get(index + 4)) + amount;
                list.set(index + 4, String.valueOf(amount));
                File f = new File("Account_Details.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                for (int i = 0; i < list.size(); i++) {
                    bw.newLine();
                    bw.flush();
                    bw.write(list.get(i));
                    bw.flush();
                }
                bw.newLine();
                bw.close();
                System.out.println("\nAmount deposited in account successfully.");
               // display();
            }
        } else {
            System.out.println("You failed to login and cannot deposit cash");
        }
    }
    public void display() {
        System.out.println("Account Details:");
        System.out.println("Name: " + list.get(index+2));
        System.out.println("Age: " + list.get(index+1));
        System.out.println("CNIC: "+list.get(index));
        System.out.println("Balance: " + amount);
    }
}

public class Main {
    public static void DisplayAll() throws IOException {
        ArrayList<String> list =  new ArrayList<>();
        Boolean EOF = false;
        String temp;
        int index = 0;
        File f = new File("Account_Details.txt");
        FileReader istream = new FileReader(f);
        BufferedReader br = new BufferedReader(istream);
        while(!EOF){
            temp = br.readLine();
            if(temp != null){
                list.add(temp);
            }
            else{
                EOF=true;
            }
        }
        br.close();

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i)==""){
                list.remove(i);
            }
        }

        for (int i=0 ; i<list.size() ; i=i+6){
            System.out.println("Account Number: "+list.get(i+3));
            System.out.println("Name: "+list.get(i+2));
            System.out.println("Age: "+list.get(i+1));
            System.out.println("CNIC: "+list.get(i));
            System.out.println("Balance: "+list.get(i+4));
            System.out.println("*------------------------------------------------------------------------------------------*");
         }
       // System.out.println(list);
    }
    public static void main(String[] args) throws IOException {
        Scanner Input = new Scanner(System.in);
        BankAccount BA = new BankAccount();
        ATM atm = new ATM();
        int Mode1=0,Mode2=0,Mode3=0;
        for(;Mode1!=4;){
            System.out.println("\n1. Account Handling");
            System.out.println("2. Access ATM");
            System.out.println("3. Display all accounts(For Admin ONLY)");
            System.out.println("4. Exit");
            System.out.printf("Select your option: ");
            Mode1= Input.nextInt();
            switch(Mode1){
                case 1:{
                    for(;Mode2!=4;) {
                        System.out.println("\n1. Create account");
                        System.out.println("2. Delete account");
                        System.out.println("3. Update account");
                        System.out.println("4. Exit");
                        System.out.printf("Select your option: ");
                        Mode2= Input.nextInt();
                        switch(Mode2){
                            case 1:{
                                BA.CreateAccount();
                                BA.filingWrite();
                                break;
                            }
                            case 2:{
                                String cnic;
                                System.out.printf("\nEnter CNIC of account you want to delete: ");
                                cnic= Input.next();
                                BA.remove(cnic);
                                break;
                            }
                            case 3:{
                                String cnic;
                                String balancecond,atmpincond;
                                boolean balancecheck=false,atmpincheck=false;
                                System.out.printf("\nEnter CNIC of account you want to update: ");
                                cnic= Input.next();
                                System.out.println("Do you want to update your balance(yes or no)?");
                                balancecond= Input.next();
                                System.out.println("Do you want to update your ATM code(yes or no)?");
                                atmpincond= Input.next();
                                if(balancecond.compareToIgnoreCase("yes")==0){
                                    balancecheck=true;
                                }
                                if(atmpincond.compareToIgnoreCase("yes")==0){
                                    atmpincheck=true;
                                }
                                BA.update(cnic,atmpincheck,balancecheck);
                                break;
                            }
                            case 4:{
                                continue;
                            }
                            default:{
                                System.out.println("\nKindly enter correct option.");
                                break;
                            }
                        }
                    }
                    break;
                }
                case 2:{
                    atm.login();
                    for (;Mode3!=3;) {
                        System.out.println("\n1. Deposit cash");
                        System.out.println("2. Withdraw cash");
                        System.out.println("3. Exit");
                        System.out.printf("\nSelect your option: ");
                        Mode3= Input.nextInt();
                        switch (Mode3){
                            case 1:{
                                atm.Deposit();
                                break;
                            }
                            case 2:{
                                atm.withDraw();
                                break;
                            }
                            case 3:{
                                continue;
                            }
                            default:{
                                System.out.println("\nKindly enter valid option.");
                                break;
                            }
                        }
                    }
                    break;
                }
                case 3:{
                    int countaccess=0;
                    for (;countaccess<3;) {
                        System.out.println("\nThis functionality is for selected individuals only.");
                        System.out.printf("Enter username(case sensitive): ");
                        String UserName= Input.next();                                              //UserName is Admin
                        System.out.printf("Enter password: ");
                        String Password= Input.next();                                              //Password is 123
                        if(UserName.compareTo("Admin")==0 && Password.compareTo("123")==0){
                            DisplayAll();
                            break;
                        }
                        else if(countaccess==3){
                            break;
                        }
                        else{
                            countaccess++;
                            System.out.println("Incorrect password or username.");
                            System.out.println("You have "+(3-countaccess)+" attempts left.");
                        }
                    }
                    if(countaccess==3){
                        Mode1=4;
                        break;
                    }
                    break;
                }
                case 4:{
                    continue;
                }
                default:{
                    System.out.println("\n\nKindly enter correct option.");
                    break;
                }
            }
        }
        System.out.println("\n\nThank you for using Bank Ammar ltd.");
        System.out.println("Visit us again.");
    }
}


