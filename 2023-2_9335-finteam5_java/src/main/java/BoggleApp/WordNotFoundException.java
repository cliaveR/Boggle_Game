package BoggleApp;


/**
* BoggleApp/WordNotFoundException.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from boggle.idl
* Thursday, May 16, 2024 3:05:57 PM SGT
*/

public final class WordNotFoundException extends org.omg.CORBA.UserException
{
  public String message = null;

  public WordNotFoundException ()
  {
    super(WordNotFoundExceptionHelper.id());
  } // ctor

  public WordNotFoundException (String _message)
  {
    super(WordNotFoundExceptionHelper.id());
    message = _message;
  } // ctor


  public WordNotFoundException (String $reason, String _message)
  {
    super(WordNotFoundExceptionHelper.id() + "  " + $reason);
    message = _message;
  } // ctor

} // class WordNotFoundException
