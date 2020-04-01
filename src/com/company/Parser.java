package com.company;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {
    ArrayList<String> ParsedCommand;
    DiskAllocator AllocTech;
    VirtualFileSystem VFS;
    //public Parser(){}  ///empty constructor TODO to be deleted
    public Parser(DiskAllocator Disk){
        this.VFS = new VirtualFileSystem(Disk);
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
        switch (Command){
            case "CreateFile":
                VFS.CreateFile(ParsedCommand.get(1), Integer.parseInt(ParsedCommand.get(2)));
                break;
            case "CreateFolder":
                VFS.CreateFolder(ParsedCommand.get(1));
                break;
            case "DeleteFile":
                VFS.DeleteFile(ParsedCommand.get(1));
                break;
            case "DeleteFolder":
                VFS.DeleteFolder(ParsedCommand.get(1));
                break;
            case "DisplayDiskStatus":
                VFS.DisplayDiskStatus();
                break;
            case "DisplayDiskStructure":
                VFS.DisplayDiskStructure();
                break;
            case "exit":
                VFS.CloseFileSystem();
                break;
            default:
                System.out.println("no such command");
        }
    }
}
