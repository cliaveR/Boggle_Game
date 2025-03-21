package BoggleApp;


/**
* BoggleApp/PlayerHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from boggle.idl
* Thursday, May 16, 2024 3:05:57 PM SGT
*/

abstract public class PlayerHelper
{
  private static String  _id = "IDL:BoggleApp/Player:1.0";

  public static void insert (org.omg.CORBA.Any a, BoggleApp.Player that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static BoggleApp.Player extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [3];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[0] = new org.omg.CORBA.StructMember (
            "pid",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[1] = new org.omg.CORBA.StructMember (
            "username",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[2] = new org.omg.CORBA.StructMember (
            "points",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (BoggleApp.PlayerHelper.id (), "Player", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static BoggleApp.Player read (org.omg.CORBA.portable.InputStream istream)
  {
    BoggleApp.Player value = new BoggleApp.Player ();
    value.pid = istream.read_long ();
    value.username = istream.read_string ();
    value.points = istream.read_long ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, BoggleApp.Player value)
  {
    ostream.write_long (value.pid);
    ostream.write_string (value.username);
    ostream.write_long (value.points);
  }

}
