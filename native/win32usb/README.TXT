Win32 USB Native implementation:

This implementation requires the Lego USB driver (available on the CD that accompanies the 2.0 kit) to be already installed. This implementation is only a wrapper implementation to that driver and allows interfacing to Java via JNI.

The project was developed using Microsoft's Developer's Studio (Visual C++ 6.0) with the supplied project file (win32usb.dsp) 

The header file supplied was created using javah utility from the JDK from the Java source file (Win32USBPort.java).

The actual native implementation resides in Win32USBPort.c

Note: since this implementation interfaces with the actual USB driver via standard Win32 I/O methods it is entirely possible that this code may work with other USB drivers for different devices that also provide Win32 I/O access. 

For bug reports, feature enhancements or general implementation questions feel free to email me at dario@escape.com
