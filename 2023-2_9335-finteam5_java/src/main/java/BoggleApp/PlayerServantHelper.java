package BoggleApp;


/**
* BoggleApp/PlayerServantHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from boggle.idl
* Thursday, May 16, 2024 3:05:57 PM SGT
*/

abstract public class PlayerServantHelper
{
  private static String  _id = "IDL:BoggleApp/PlayerServant:1.0";

  public static void insert (org.omg.CORBA.Any a, BoggleApp.PlayerServant that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static BoggleApp.PlayerServant extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (BoggleApp.PlayerServantHelper.id (), "PlayerServant");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static BoggleApp.PlayerServant read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_PlayerServantStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, BoggleApp.PlayerServant value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static BoggleApp.PlayerServant narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof BoggleApp.PlayerServant)
      return (BoggleApp.PlayerServant)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      BoggleApp._PlayerServantStub stub = new BoggleApp._PlayerServantStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static BoggleApp.PlayerServant unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof BoggleApp.PlayerServant)
      return (BoggleApp.PlayerServant)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      BoggleApp._PlayerServantStub stub = new BoggleApp._PlayerServantStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
