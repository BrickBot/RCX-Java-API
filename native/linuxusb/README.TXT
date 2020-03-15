Linux USB Native implementation:

This implementation is not yet available but one can take the approach of the Win32 implementation as a guideline.

Currently there is a Lego USB driver project underway at:

http://legousb.sourceforge.net/legousbtower/index.shtml

At the time of this writing it is not quite ready yet but close.

The header file supplied was created using javah utility from the JDK from the Java source file (LinuxUSBPort.java).

The actual native implementation would include the header file and implement the function prototypes defined.

These methods would simply call the functions defined by the LegoUSB Project above. For example, one would simply implement the read and write methods of the header file supplied by having the methods call LegoUSB Project's functions:

 ssize_t tower_read (struct file *file, char *buffer, size_t count, loff_t *ppos);
 ssize_t tower_write(struct file *file, const char *buffer, size_t count, loff_t *ppos);


If you are a Linux developer who can implement or test this please email me at dario@escape.com and I'll give you credit here for this implementation.
