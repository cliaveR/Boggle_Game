package BoggleApp;

/**
* BoggleApp/WordNotFoundExceptionHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from boggle.idl
* Thursday, May 16, 2024 3:05:57 PM SGT
*/

public final class WordNotFoundExceptionHolder implements org.omg.CORBA.portable.Streamable
{
  public BoggleApp.WordNotFoundException value = null;

  public WordNotFoundExceptionHolder ()
  {
  }

  public WordNotFoundExceptionHolder (BoggleApp.WordNotFoundException initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = BoggleApp.WordNotFoundExceptionHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    BoggleApp.WordNotFoundExceptionHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return BoggleApp.WordNotFoundExceptionHelper.type ();
  }

}
