package BoggleApp;


/**
* BoggleApp/WordListHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from boggle.idl
* Thursday, May 16, 2024 3:05:57 PM SGT
*/

public final class WordListHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public WordListHolder ()
  {
  }

  public WordListHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = BoggleApp.WordListHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    BoggleApp.WordListHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return BoggleApp.WordListHelper.type ();
  }

}
