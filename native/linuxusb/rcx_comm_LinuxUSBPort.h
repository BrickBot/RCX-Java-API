/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class rcx_comm_LinuxUSBPort */

#ifndef _Included_rcx_comm_LinuxUSBPort
#define _Included_rcx_comm_LinuxUSBPort
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: LibLoaded */
/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1open
  (JNIEnv *, jobject, jstring);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _read
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1read__
  (JNIEnv *, jobject);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _read
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1read___3BII
  (JNIEnv *, jobject, jbyteArray, jint, jint);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _write
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1write__I
  (JNIEnv *, jobject, jint);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1write___3BII
  (JNIEnv *, jobject, jbyteArray, jint, jint);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1close
  (JNIEnv *, jobject);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _setReceiveTimeout
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1setReceiveTimeout
  (JNIEnv *, jobject, jint);

/*
 * Class:     rcx_comm_LinuxUSBPort
 * Method:    _available
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_LinuxUSBPort__1available
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif