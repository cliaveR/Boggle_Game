package BoggleApp;


/**
* BoggleApp/Player.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from boggle.idl
* Thursday, May 16, 2024 3:05:57 PM SGT
*/

public final class Player implements org.omg.CORBA.portable.IDLEntity
{
  public int pid = (int)0;
  public String username = null;
  public int points = (int)0;

  public Player ()
  {
  } // ctor

  public Player (int _pid, String _username, int _points)
  {
    pid = _pid;
    username = _username;
    points = _points;
  } // ctor

} // class Player
