 // 入出力ストリームを使うので，java.io.* を import
 import java.io.*;
 // ソケットを使うので java.net.* を import 
import java.net.*;

// ユーザのキーボード入力を受け取って, サーバに送るスレッドと 
// サーバと接続したソケットからメッセージを受け取って表示するスレッド
// を共用したクラス
class Sender implements Runnable{
 // 入力ストリーム
 BufferedReader in;
 // 出力ストリーム
 PrintWriter out;
 // コンストラクタ
 public Sender(BufferedReader in,PrintWriter out){
   // 左辺の this.inはインスタンス変数の in, 右辺の inは引数の in
   this.in=in;
   this.out=out;
 }
 // スレッドが start すると,これが呼ばれて,動き続ける．
 public void run(){
   try{
     String s;
     // 入力ストリームから一行入力
     while((s=in.readLine())!=null){
       // 出力ストリームに一行出力
   out.println(s);
     }
   }
   catch(Exception e){
     System.err.println(e);
   }
 }
}
// java ChatClient ホスト名 ポート番号
// と呼び出す
class ChatClient{
 public static void main(String args[]){
   // ホスト名
   String hostName=args[0];
   // ポート番号を文字列から整数に変換
   int port=Integer.parseInt(args[1]);
   try{
     // サーバと接続するためのソケットを作る
     Socket sock=new Socket(hostName,port);
     // ユーザからの入力のストリームの作成
     BufferedReader ui=new BufferedReader(new InputStreamReader(System.in));
     // サーバからの入力のストリームの作成
     BufferedReader si=new BufferedReader(new InputStreamReader(sock.getInputStream()));
     // サーバへの出力のストリームの作成
     PrintWriter so=new PrintWriter(sock.getOutputStream(),true);
     // ユーザへの出力のストリームの作成
     PrintWriter uo=new PrintWriter(System.out,true);
     // ユーザ入力をサーバに転送するスレッドを作成し, start
     new Thread(new Sender(ui,so)).start();
     // サーバからの入力をユーザに転送するスレッドを作成し, start
     new Thread(new Sender(si,uo)).start();
   }
   catch(IOException e){
     System.err.println(e);
   }
 }
}