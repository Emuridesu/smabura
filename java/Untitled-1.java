 // 入出力ストリームを使うので，java.io.* を import
 import java.io.*;
 // ソケットを使うので java.net.* を import 
import java.net.*;

 // 一人のクライアントとの通信を担当するスレッド
 // スレッド上で走らせるため Runnable インタフェースを実装
class Worker implements Runnable{
   // 通信のためのソケット
 Socket sock;
   // そのソケットから作成した入出力用のストリーム
 PrintWriter out;
 BufferedReader in;
   // サーバ本体のメソッドを呼び出すために記憶
 ChatServer chatServer;
   // 担当するクライアントの番号
 int n;
   // コンストラクタ
 public Worker(int n,Socket s,ChatServer cs){ 
   this.n=n;
   chatServer=cs; 
   sock=s; 
   out=null; 
   in=null;
 }
   // 対応するスレッドが start した時に呼ばれる．
 public void run(){
   System.out.println("Thread running:"+Thread.currentThread());
   try{
       // ソケットからストリームの作成
     out = new PrintWriter(sock.getOutputStream(),true);
     in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
     String s=null;
       // ソケットからの入力があったら，
     while((s=in.readLine())!=null){
         // クライアント全体に送る．
       chatServer.sendAll("["+n+"]"+s);
     }
       // 自分自身をテーブルから取り除く
     chatServer.remove(n);
       // ソケットを閉じる
     sock.close();
   }
   catch(IOException ioe){
     System.out.println(ioe);
     chatServer.remove(n);
   }
 }
   // 対応するソケットに文字列を送る
 public void send(String s){
   out.println(s);
 }
}
class ChatServer{
   // 各クライアントを記憶する配列．
 Worker workers[];
   // コンストラクタ
 public ChatServer(){
     // ポート番号を 4444にする．同じマシンで同じポートを使うことは
     // できないので，ユーザごとに変えること(1023以下は使えない)
   int port=4444;
     // 配列を作成
   workers=new Worker[100];
   Socket sock;
   try{
       // ServerSocketを作成
     ServerSocket servsock=new ServerSocket(port);
       // 無限ループ，breakが来るまで
     while(true){
         // クライアントからのアクセスをうけつけた．
       sock=servsock.accept();
       int i;
         // 配列すべてについて
       for(i=0;i< workers.length;i++){
           // 空いている要素があったら，
         if(workers[i]==null){
             // Workerを作って
           workers[i]=new Worker(i,sock,this);
             // 対応するスレッドを走らせる
           new Thread(workers[i]).start();
           break;
         }
       }
       if(i==workers.length){
         System.out.println("Can't serve");
       }
     }
   } catch(IOException ioe){
     System.out.println(ioe);
   }
 }
 public static void main(String args[]) throws IOException{
     // インスタンスを1つだけ作る．
   new ChatServer();
 }
   // synchronized は，同期のためのキーワード．つけなくても動くことはある．
 public synchronized void sendAll(String s){
   int i;
   for(i=0;i< workers.length;i++){
       // workers[i]が空でなければ文字列を送る
     if(workers[i]!=null)
       workers[i].send(s);
   }
 }
   // クライアント n が抜けたこと記録し，他のユーザに送る．
 public void remove(int n){
   workers[n]=null;
   sendAll("quiting ["+n+"]");
 }
}