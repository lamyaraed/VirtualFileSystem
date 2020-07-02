package ParserManagement;

import java.util.ArrayList;
import java.util.StringTokenizer;

import AllocationManagement.DiskAllocator;
import UserManagement.UserManager;
import VFileManagement.VirtualFileSystem;

public class Parser {
    ArrayList<String> ParsedCommand;
    DiskAllocator AllocTech;
    VirtualFileSystem VFS;
    UserManager UM;
    //public Parser(){}  ///empty constructor TODO to be deleted
    public Parser(DiskAllocator Disk){
        this.VFS = new VirtualFileSystem(Disk);
        UM = VFS.getUserManager();
    }
    public void addCommand(String Command){
        String Cmd;

        ParsedCommand = new ArrayList<>();
        StringTokenizer parseString = new StringTokenizer(Command);
        while(parseString.hasMoreTokens()){
            Cmd = parseString.nextToken();
            ParsedCommand.add(Cmd);
        }
        CheckCommand();
    }
    void CheckCommand(){
        String Command = ParsedCommand.get(0);
        try {
	        switch (Command){
	            case "CreateFile":
	                if(UM.HasCapabilityInDirectory(GetParentDirectory(ParsedCommand.get(1)),"CreateFile")) {
	                    VFS.CreateFile(ParsedCommand.get(1), Integer.parseInt(ParsedCommand.get(2)));
	                }
	                else
	                    System.out.println("Sorry you dont have permission to Create a File");
	                break;
	            case "CreateFolder":
	                if(UM.HasCapabilityInDirectory(GetParentDirectory(ParsedCommand.get(1)),"CreateFolder")) {
	                    VFS.CreateFolder(ParsedCommand.get(1));
	                }
	                else
	                    System.out.println("Sorry you dont have permission to Create a Folder");
	                break;
	            case "DeleteFile":
	                if(UM.HasCapabilityInDirectory(ParsedCommand.get(1),"DeleteFile")) {
	                    VFS.DeleteFile(ParsedCommand.get(1));
	                }
	                else
	                    System.out.println("Sorry you dont have permission to Delete a File");
	                break;
	            case "DeleteFolder":
	                if(UM.HasCapabilityInDirectory(ParsedCommand.get(1),"DeleteFolder")) {
	                    VFS.DeleteFolder(ParsedCommand.get(1));
	                }
	                else
	                    System.out.println("Sorry you dont have permission to Delete a Folder");
	                break;
	            case "DisplayDiskStatus":
	                VFS.DisplayDiskStatus();
	                break;
	            case "DisplayDiskStructure":
	                VFS.DisplayDiskStructure();
	                break;
	            case "TellUser":
	                UM.TellUser();
	                break;
	            case "CreateUser" :
	                UM.CreateUser(ParsedCommand.get(1), ParsedCommand.get(2));
	                break;
	            case "DeleteUser":
	                UM.DeleteUser(ParsedCommand.get(1));
	                break;
	            case "Grant":
	                UM.GrantUser(ParsedCommand.get(1) ,ParsedCommand.get(2) +"/", ParsedCommand.get(3));
	                break;
	            case "Login":
	                UM.LoginUser(ParsedCommand.get(1), ParsedCommand.get(2));
	                break;
	            case "exit":
	                VFS.CloseFileSystem();
	                break;
	            default:
	                System.out.println("no such command");
	        }
        }
        catch (Exception e) {
			// TODO: handle exception
        //	System.out.println(e.getMessage());
        	System.out.println("invalid command");
		}
    }

    String GetParentDirectory(String Path){
        String[] Parsed = Path.split("/");
        String ParentDirectoryPath = Parsed[0] ;

        for(int i = 1; i < Parsed.length - 1;i++){
            ParentDirectoryPath+= "/" +  Parsed[i];
        }
       // System.out.println(ParentDirectoryPath);
        return ParentDirectoryPath;
    }

}
