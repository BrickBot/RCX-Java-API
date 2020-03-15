Macintosh USB Native implementation:

This implementation is not yet available but one can take the approach of the Win32 implementation that requires the Lego USB driver (available on the CD that accompanies the 2.0 kit. This implementation would be only a wrapper implementation to that driver allowing interfacing to Java via JNI. Or one could write the native USB driver itself and interface it with Java using the supplied template JNI header file.

For information on writing to JNI (Java Native Interface) for the Macintosh please note the following resources:

http://developer.apple.com/technotes/tn/tn1155.html
http://informagen.com/Java/ExamplesJNI/

The header file supplied was created using javah utility from the JDK from the Java source file (MacUSBPort.java).

The actual native implementation would include the header file and implement the function prototypes defined.

If you are a Macintosh developer who can implement or test this please email me at dario@escape.com and I'll give you credit here for this implementation.
