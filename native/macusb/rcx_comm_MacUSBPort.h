/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class rcx_comm_MacUSBPort */

#ifndef _Included_rcx_comm_MacUSBPort
#define _Included_rcx_comm_MacUSBPort
#ifdef __cplusplus
extern "C" {
#endif
/* Inaccessible static: LibLoaded */
/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1open
  (JNIEnv *, jobject, jstring);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _read
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1read__
  (JNIEnv *, jobject);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _read
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1read___3BII
  (JNIEnv *, jobject, jbyteArray, jint, jint);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _write
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1write__I
  (JNIEnv *, jobject, jint);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1write___3BII
  (JNIEnv *, jobject, jbyteArray, jint, jint);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1close
  (JNIEnv *, jobject);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _setReceiveTimeout
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1setReceiveTimeout
  (JNIEnv *, jobject, jint);

/*
 * Class:     rcx_comm_MacUSBPort
 * Method:    _available
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_rcx_comm_MacUSBPort__1available
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif