package com.lsx.component.mqttBroker.mqtt.common.ssl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * A bogus key store which provides all the required information to create an
 * example SSL connection.
 * 
 * To generate a bogus key store:
 * 
 * <pre>
 * keytool -genkey -alias securesocket \ -keysize 2048 -validity 36500 \  -keyalg RSA -dname "CN=securesocket" \   -keypass inc0rrect -storepass mu$tch8ng3 \  -keystore cert.jks
 *
 *
 *
 *
 * </pre>
 */
public class SecureSocketKeyStore {

	private static final byte[] CERT_BYTES = { (byte) 254, (byte) 237,
			(byte) 254, (byte) 237, (byte) 0, (byte) 0, (byte) 0, (byte) 2,
			(byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0,
			(byte) 0, (byte) 1, (byte) 0, (byte) 12, (byte) 115, (byte) 101,
			(byte) 99, (byte) 117, (byte) 114, (byte) 101, (byte) 115,
			(byte) 111, (byte) 99, (byte) 107, (byte) 101, (byte) 116,
			(byte) 0, (byte) 0, (byte) 1, (byte) 69, (byte) 231, (byte) 201,
			(byte) 156, (byte) 140, (byte) 0, (byte) 0, (byte) 5, (byte) 0,
			(byte) 48, (byte) 130, (byte) 4, (byte) 252, (byte) 48, (byte) 14,
			(byte) 6, (byte) 10, (byte) 43, (byte) 6, (byte) 1, (byte) 4,
			(byte) 1, (byte) 42, (byte) 2, (byte) 17, (byte) 1, (byte) 1,
			(byte) 5, (byte) 0, (byte) 4, (byte) 130, (byte) 4, (byte) 232,
			(byte) 221, (byte) 18, (byte) 203, (byte) 171, (byte) 175,
			(byte) 82, (byte) 132, (byte) 227, (byte) 115, (byte) 143,
			(byte) 38, (byte) 191, (byte) 42, (byte) 202, (byte) 130,
			(byte) 171, (byte) 75, (byte) 6, (byte) 161, (byte) 120,
			(byte) 204, (byte) 61, (byte) 106, (byte) 160, (byte) 81, (byte) 9,
			(byte) 204, (byte) 153, (byte) 166, (byte) 38, (byte) 246,
			(byte) 13, (byte) 43, (byte) 19, (byte) 100, (byte) 132, (byte) 45,
			(byte) 90, (byte) 143, (byte) 1, (byte) 231, (byte) 182, (byte) 89,
			(byte) 228, (byte) 183, (byte) 17, (byte) 95, (byte) 129,
			(byte) 229, (byte) 42, (byte) 182, (byte) 126, (byte) 114,
			(byte) 76, (byte) 124, (byte) 123, (byte) 246, (byte) 152,
			(byte) 0, (byte) 141, (byte) 212, (byte) 111, (byte) 52,
			(byte) 243, (byte) 112, (byte) 31, (byte) 117, (byte) 124,
			(byte) 142, (byte) 24, (byte) 59, (byte) 198, (byte) 164,
			(byte) 253, (byte) 21, (byte) 177, (byte) 189, (byte) 74,
			(byte) 218, (byte) 110, (byte) 83, (byte) 154, (byte) 49,
			(byte) 186, (byte) 159, (byte) 173, (byte) 202, (byte) 94,
			(byte) 174, (byte) 183, (byte) 223, (byte) 119, (byte) 109,
			(byte) 110, (byte) 72, (byte) 93, (byte) 208, (byte) 195,
			(byte) 19, (byte) 89, (byte) 33, (byte) 34, (byte) 186, (byte) 12,
			(byte) 86, (byte) 156, (byte) 156, (byte) 210, (byte) 111,
			(byte) 110, (byte) 44, (byte) 106, (byte) 36, (byte) 67,
			(byte) 168, (byte) 7, (byte) 179, (byte) 244, (byte) 53,
			(byte) 134, (byte) 10, (byte) 86, (byte) 179, (byte) 34, (byte) 60,
			(byte) 184, (byte) 179, (byte) 162, (byte) 69, (byte) 24,
			(byte) 168, (byte) 100, (byte) 183, (byte) 206, (byte) 64,
			(byte) 4, (byte) 32, (byte) 66, (byte) 237, (byte) 228, (byte) 92,
			(byte) 6, (byte) 213, (byte) 141, (byte) 147, (byte) 198,
			(byte) 141, (byte) 216, (byte) 41, (byte) 0, (byte) 101, (byte) 65,
			(byte) 41, (byte) 185, (byte) 128, (byte) 229, (byte) 107,
			(byte) 25, (byte) 89, (byte) 148, (byte) 16, (byte) 194,
			(byte) 101, (byte) 100, (byte) 243, (byte) 147, (byte) 77,
			(byte) 230, (byte) 11, (byte) 151, (byte) 99, (byte) 124,
			(byte) 55, (byte) 195, (byte) 185, (byte) 30, (byte) 234,
			(byte) 83, (byte) 61, (byte) 109, (byte) 131, (byte) 156,
			(byte) 244, (byte) 133, (byte) 66, (byte) 39, (byte) 153, (byte) 9,
			(byte) 34, (byte) 218, (byte) 201, (byte) 143, (byte) 190,
			(byte) 127, (byte) 119, (byte) 102, (byte) 6, (byte) 83,
			(byte) 134, (byte) 96, (byte) 170, (byte) 79, (byte) 196,
			(byte) 214, (byte) 47, (byte) 215, (byte) 37, (byte) 250,
			(byte) 64, (byte) 8, (byte) 165, (byte) 203, (byte) 44, (byte) 53,
			(byte) 113, (byte) 147, (byte) 251, (byte) 29, (byte) 26,
			(byte) 38, (byte) 193, (byte) 11, (byte) 223, (byte) 212,
			(byte) 114, (byte) 96, (byte) 162, (byte) 39, (byte) 48,
			(byte) 200, (byte) 172, (byte) 182, (byte) 254, (byte) 180,
			(byte) 198, (byte) 11, (byte) 128, (byte) 75, (byte) 74, (byte) 93,
			(byte) 226, (byte) 157, (byte) 80, (byte) 14, (byte) 9, (byte) 217,
			(byte) 236, (byte) 205, (byte) 153, (byte) 35, (byte) 242,
			(byte) 130, (byte) 140, (byte) 25, (byte) 16, (byte) 156,
			(byte) 247, (byte) 230, (byte) 5, (byte) 247, (byte) 0, (byte) 34,
			(byte) 196, (byte) 15, (byte) 118, (byte) 255, (byte) 185,
			(byte) 199, (byte) 59, (byte) 99, (byte) 27, (byte) 187, (byte) 83,
			(byte) 81, (byte) 12, (byte) 71, (byte) 69, (byte) 127, (byte) 130,
			(byte) 164, (byte) 97, (byte) 195, (byte) 216, (byte) 215,
			(byte) 61, (byte) 29, (byte) 196, (byte) 62, (byte) 160,
			(byte) 188, (byte) 209, (byte) 173, (byte) 230, (byte) 0,
			(byte) 204, (byte) 225, (byte) 1, (byte) 5, (byte) 42, (byte) 223,
			(byte) 232, (byte) 187, (byte) 190, (byte) 67, (byte) 126,
			(byte) 235, (byte) 178, (byte) 218, (byte) 179, (byte) 46,
			(byte) 186, (byte) 156, (byte) 186, (byte) 6, (byte) 191,
			(byte) 68, (byte) 239, (byte) 31, (byte) 16, (byte) 204, (byte) 24,
			(byte) 68, (byte) 164, (byte) 88, (byte) 10, (byte) 174, (byte) 26,
			(byte) 54, (byte) 187, (byte) 149, (byte) 132, (byte) 128,
			(byte) 173, (byte) 165, (byte) 8, (byte) 69, (byte) 96, (byte) 49,
			(byte) 57, (byte) 223, (byte) 110, (byte) 29, (byte) 215,
			(byte) 98, (byte) 42, (byte) 15, (byte) 153, (byte) 228,
			(byte) 216, (byte) 61, (byte) 160, (byte) 230, (byte) 34,
			(byte) 40, (byte) 232, (byte) 136, (byte) 139, (byte) 140,
			(byte) 236, (byte) 251, (byte) 119, (byte) 242, (byte) 199,
			(byte) 167, (byte) 61, (byte) 141, (byte) 89, (byte) 29, (byte) 82,
			(byte) 114, (byte) 229, (byte) 198, (byte) 27, (byte) 133,
			(byte) 87, (byte) 0, (byte) 53, (byte) 69, (byte) 42, (byte) 91,
			(byte) 174, (byte) 82, (byte) 244, (byte) 160, (byte) 82,
			(byte) 142, (byte) 221, (byte) 106, (byte) 151, (byte) 241,
			(byte) 214, (byte) 64, (byte) 14, (byte) 28, (byte) 2, (byte) 3,
			(byte) 145, (byte) 143, (byte) 18, (byte) 165, (byte) 247,
			(byte) 178, (byte) 211, (byte) 16, (byte) 222, (byte) 76,
			(byte) 60, (byte) 119, (byte) 130, (byte) 199, (byte) 230,
			(byte) 229, (byte) 3, (byte) 22, (byte) 100, (byte) 135,
			(byte) 103, (byte) 60, (byte) 181, (byte) 191, (byte) 56,
			(byte) 249, (byte) 181, (byte) 169, (byte) 210, (byte) 25,
			(byte) 152, (byte) 201, (byte) 226, (byte) 119, (byte) 71,
			(byte) 204, (byte) 70, (byte) 220, (byte) 103, (byte) 46,
			(byte) 166, (byte) 125, (byte) 40, (byte) 86, (byte) 208,
			(byte) 114, (byte) 138, (byte) 24, (byte) 27, (byte) 219,
			(byte) 123, (byte) 161, (byte) 52, (byte) 14, (byte) 38,
			(byte) 244, (byte) 112, (byte) 238, (byte) 121, (byte) 90,
			(byte) 34, (byte) 157, (byte) 131, (byte) 53, (byte) 245,
			(byte) 162, (byte) 89, (byte) 188, (byte) 6, (byte) 202,
			(byte) 164, (byte) 130, (byte) 34, (byte) 232, (byte) 74,
			(byte) 45, (byte) 137, (byte) 164, (byte) 200, (byte) 197,
			(byte) 247, (byte) 64, (byte) 110, (byte) 122, (byte) 49,
			(byte) 116, (byte) 137, (byte) 253, (byte) 170, (byte) 232,
			(byte) 120, (byte) 26, (byte) 171, (byte) 228, (byte) 229,
			(byte) 49, (byte) 56, (byte) 56, (byte) 106, (byte) 110, (byte) 12,
			(byte) 109, (byte) 93, (byte) 105, (byte) 241, (byte) 196,
			(byte) 11, (byte) 18, (byte) 89, (byte) 108, (byte) 146,
			(byte) 224, (byte) 161, (byte) 181, (byte) 236, (byte) 74,
			(byte) 128, (byte) 24, (byte) 239, (byte) 22, (byte) 146, (byte) 0,
			(byte) 69, (byte) 182, (byte) 246, (byte) 43, (byte) 59,
			(byte) 208, (byte) 33, (byte) 48, (byte) 81, (byte) 0, (byte) 70,
			(byte) 225, (byte) 222, (byte) 122, (byte) 178, (byte) 138,
			(byte) 12, (byte) 207, (byte) 233, (byte) 164, (byte) 13,
			(byte) 176, (byte) 123, (byte) 95, (byte) 68, (byte) 238,
			(byte) 134, (byte) 66, (byte) 95, (byte) 194, (byte) 192,
			(byte) 225, (byte) 244, (byte) 14, (byte) 78, (byte) 53,
			(byte) 189, (byte) 217, (byte) 229, (byte) 203, (byte) 192,
			(byte) 34, (byte) 38, (byte) 169, (byte) 63, (byte) 239,
			(byte) 128, (byte) 172, (byte) 143, (byte) 75, (byte) 7,
			(byte) 237, (byte) 125, (byte) 179, (byte) 235, (byte) 229,
			(byte) 98, (byte) 8, (byte) 211, (byte) 237, (byte) 116, (byte) 75,
			(byte) 27, (byte) 211, (byte) 131, (byte) 245, (byte) 89,
			(byte) 150, (byte) 35, (byte) 49, (byte) 207, (byte) 113,
			(byte) 237, (byte) 114, (byte) 125, (byte) 134, (byte) 191,
			(byte) 110, (byte) 30, (byte) 119, (byte) 131, (byte) 175,
			(byte) 166, (byte) 201, (byte) 255, (byte) 200, (byte) 1,
			(byte) 126, (byte) 163, (byte) 172, (byte) 52, (byte) 118,
			(byte) 184, (byte) 221, (byte) 165, (byte) 167, (byte) 165,
			(byte) 20, (byte) 135, (byte) 32, (byte) 222, (byte) 188,
			(byte) 250, (byte) 64, (byte) 161, (byte) 67, (byte) 236,
			(byte) 212, (byte) 131, (byte) 44, (byte) 32, (byte) 70, (byte) 0,
			(byte) 24, (byte) 178, (byte) 83, (byte) 155, (byte) 145,
			(byte) 136, (byte) 131, (byte) 120, (byte) 181, (byte) 164,
			(byte) 155, (byte) 172, (byte) 41, (byte) 213, (byte) 164,
			(byte) 98, (byte) 169, (byte) 152, (byte) 184, (byte) 170,
			(byte) 107, (byte) 7, (byte) 21, (byte) 228, (byte) 175,
			(byte) 192, (byte) 238, (byte) 68, (byte) 197, (byte) 119,
			(byte) 228, (byte) 225, (byte) 156, (byte) 235, (byte) 241,
			(byte) 172, (byte) 171, (byte) 236, (byte) 128, (byte) 78,
			(byte) 117, (byte) 152, (byte) 123, (byte) 93, (byte) 156,
			(byte) 57, (byte) 238, (byte) 211, (byte) 188, (byte) 47,
			(byte) 62, (byte) 45, (byte) 127, (byte) 58, (byte) 38, (byte) 29,
			(byte) 131, (byte) 95, (byte) 85, (byte) 149, (byte) 112,
			(byte) 215, (byte) 207, (byte) 41, (byte) 201, (byte) 30,
			(byte) 149, (byte) 73, (byte) 245, (byte) 179, (byte) 176,
			(byte) 246, (byte) 203, (byte) 204, (byte) 252, (byte) 13,
			(byte) 98, (byte) 151, (byte) 93, (byte) 87, (byte) 241,
			(byte) 166, (byte) 46, (byte) 249, (byte) 148, (byte) 49,
			(byte) 141, (byte) 136, (byte) 49, (byte) 77, (byte) 250,
			(byte) 191, (byte) 157, (byte) 90, (byte) 84, (byte) 51,
			(byte) 129, (byte) 133, (byte) 66, (byte) 253, (byte) 99,
			(byte) 243, (byte) 34, (byte) 142, (byte) 197, (byte) 4,
			(byte) 126, (byte) 7, (byte) 217, (byte) 126, (byte) 205,
			(byte) 250, (byte) 141, (byte) 231, (byte) 225, (byte) 203,
			(byte) 171, (byte) 246, (byte) 201, (byte) 48, (byte) 96,
			(byte) 207, (byte) 74, (byte) 253, (byte) 120, (byte) 114,
			(byte) 163, (byte) 192, (byte) 24, (byte) 12, (byte) 10,
			(byte) 210, (byte) 94, (byte) 136, (byte) 152, (byte) 185,
			(byte) 109, (byte) 87, (byte) 35, (byte) 159, (byte) 238,
			(byte) 122, (byte) 200, (byte) 107, (byte) 103, (byte) 243,
			(byte) 250, (byte) 152, (byte) 68, (byte) 66, (byte) 170, (byte) 0,
			(byte) 134, (byte) 229, (byte) 168, (byte) 182, (byte) 30,
			(byte) 89, (byte) 240, (byte) 121, (byte) 106, (byte) 148,
			(byte) 142, (byte) 49, (byte) 242, (byte) 215, (byte) 233,
			(byte) 57, (byte) 120, (byte) 204, (byte) 180, (byte) 239,
			(byte) 199, (byte) 133, (byte) 255, (byte) 71, (byte) 3,
			(byte) 132, (byte) 228, (byte) 110, (byte) 66, (byte) 227,
			(byte) 122, (byte) 82, (byte) 118, (byte) 173, (byte) 218,
			(byte) 54, (byte) 99, (byte) 167, (byte) 154, (byte) 3, (byte) 189,
			(byte) 25, (byte) 123, (byte) 169, (byte) 42, (byte) 184,
			(byte) 59, (byte) 36, (byte) 131, (byte) 206, (byte) 248,
			(byte) 90, (byte) 32, (byte) 183, (byte) 86, (byte) 62, (byte) 149,
			(byte) 107, (byte) 243, (byte) 71, (byte) 197, (byte) 124,
			(byte) 155, (byte) 214, (byte) 91, (byte) 29, (byte) 81, (byte) 28,
			(byte) 115, (byte) 98, (byte) 130, (byte) 184, (byte) 135,
			(byte) 13, (byte) 191, (byte) 147, (byte) 43, (byte) 10,
			(byte) 178, (byte) 99, (byte) 165, (byte) 210, (byte) 87,
			(byte) 87, (byte) 148, (byte) 31, (byte) 198, (byte) 129,
			(byte) 32, (byte) 181, (byte) 3, (byte) 144, (byte) 61, (byte) 5,
			(byte) 166, (byte) 252, (byte) 73, (byte) 205, (byte) 230,
			(byte) 178, (byte) 162, (byte) 46, (byte) 56, (byte) 99, (byte) 77,
			(byte) 97, (byte) 236, (byte) 121, (byte) 157, (byte) 139,
			(byte) 153, (byte) 217, (byte) 171, (byte) 19, (byte) 68,
			(byte) 36, (byte) 14, (byte) 123, (byte) 249, (byte) 101,
			(byte) 127, (byte) 184, (byte) 123, (byte) 7, (byte) 124,
			(byte) 68, (byte) 98, (byte) 34, (byte) 139, (byte) 224,
			(byte) 173, (byte) 246, (byte) 196, (byte) 180, (byte) 70,
			(byte) 207, (byte) 168, (byte) 211, (byte) 255, (byte) 84,
			(byte) 0, (byte) 174, (byte) 11, (byte) 160, (byte) 155,
			(byte) 127, (byte) 228, (byte) 81, (byte) 226, (byte) 115,
			(byte) 142, (byte) 200, (byte) 107, (byte) 4, (byte) 204,
			(byte) 219, (byte) 192, (byte) 189, (byte) 56, (byte) 127,
			(byte) 184, (byte) 187, (byte) 161, (byte) 106, (byte) 62,
			(byte) 225, (byte) 211, (byte) 115, (byte) 30, (byte) 172,
			(byte) 191, (byte) 66, (byte) 25, (byte) 66, (byte) 235,
			(byte) 107, (byte) 41, (byte) 186, (byte) 40, (byte) 239,
			(byte) 173, (byte) 11, (byte) 247, (byte) 89, (byte) 79,
			(byte) 135, (byte) 86, (byte) 73, (byte) 77, (byte) 164, (byte) 34,
			(byte) 109, (byte) 236, (byte) 56, (byte) 198, (byte) 141,
			(byte) 87, (byte) 74, (byte) 172, (byte) 56, (byte) 24, (byte) 150,
			(byte) 233, (byte) 233, (byte) 165, (byte) 122, (byte) 201,
			(byte) 112, (byte) 232, (byte) 23, (byte) 12, (byte) 166,
			(byte) 128, (byte) 114, (byte) 139, (byte) 207, (byte) 233,
			(byte) 47, (byte) 220, (byte) 172, (byte) 175, (byte) 40,
			(byte) 109, (byte) 82, (byte) 142, (byte) 130, (byte) 177,
			(byte) 50, (byte) 127, (byte) 196, (byte) 106, (byte) 172,
			(byte) 178, (byte) 71, (byte) 178, (byte) 204, (byte) 99,
			(byte) 113, (byte) 33, (byte) 189, (byte) 188, (byte) 168,
			(byte) 76, (byte) 92, (byte) 230, (byte) 211, (byte) 239,
			(byte) 75, (byte) 71, (byte) 64, (byte) 197, (byte) 26, (byte) 222,
			(byte) 19, (byte) 213, (byte) 161, (byte) 144, (byte) 20,
			(byte) 126, (byte) 192, (byte) 156, (byte) 15, (byte) 113,
			(byte) 64, (byte) 73, (byte) 7, (byte) 241, (byte) 217, (byte) 127,
			(byte) 171, (byte) 199, (byte) 66, (byte) 32, (byte) 179, (byte) 4,
			(byte) 181, (byte) 93, (byte) 121, (byte) 193, (byte) 10,
			(byte) 169, (byte) 255, (byte) 152, (byte) 199, (byte) 95,
			(byte) 177, (byte) 227, (byte) 135, (byte) 21, (byte) 64,
			(byte) 203, (byte) 9, (byte) 79, (byte) 243, (byte) 114, (byte) 2,
			(byte) 201, (byte) 157, (byte) 180, (byte) 52, (byte) 193,
			(byte) 66, (byte) 34, (byte) 155, (byte) 52, (byte) 35, (byte) 93,
			(byte) 31, (byte) 96, (byte) 77, (byte) 12, (byte) 80, (byte) 195,
			(byte) 96, (byte) 247, (byte) 251, (byte) 237, (byte) 36,
			(byte) 170, (byte) 7, (byte) 3, (byte) 251, (byte) 243, (byte) 47,
			(byte) 180, (byte) 98, (byte) 207, (byte) 176, (byte) 106,
			(byte) 237, (byte) 114, (byte) 91, (byte) 229, (byte) 56,
			(byte) 94, (byte) 154, (byte) 32, (byte) 62, (byte) 240,
			(byte) 132, (byte) 4, (byte) 144, (byte) 227, (byte) 140,
			(byte) 137, (byte) 76, (byte) 15, (byte) 117, (byte) 82,
			(byte) 223, (byte) 168, (byte) 135, (byte) 33, (byte) 91,
			(byte) 173, (byte) 4, (byte) 245, (byte) 192, (byte) 95,
			(byte) 135, (byte) 22, (byte) 138, (byte) 89, (byte) 1, (byte) 14,
			(byte) 230, (byte) 143, (byte) 195, (byte) 93, (byte) 133,
			(byte) 194, (byte) 252, (byte) 188, (byte) 31, (byte) 39,
			(byte) 162, (byte) 59, (byte) 148, (byte) 219, (byte) 213,
			(byte) 179, (byte) 195, (byte) 165, (byte) 67, (byte) 68,
			(byte) 39, (byte) 178, (byte) 143, (byte) 192, (byte) 177,
			(byte) 221, (byte) 236, (byte) 63, (byte) 40, (byte) 205,
			(byte) 26, (byte) 81, (byte) 127, (byte) 5, (byte) 213, (byte) 192,
			(byte) 22, (byte) 147, (byte) 98, (byte) 207, (byte) 153, (byte) 8,
			(byte) 108, (byte) 75, (byte) 182, (byte) 148, (byte) 0,
			(byte) 151, (byte) 15, (byte) 178, (byte) 98, (byte) 145,
			(byte) 255, (byte) 213, (byte) 142, (byte) 63, (byte) 247,
			(byte) 42, (byte) 161, (byte) 246, (byte) 21, (byte) 128,
			(byte) 47, (byte) 248, (byte) 217, (byte) 70, (byte) 195,
			(byte) 151, (byte) 236, (byte) 73, (byte) 153, (byte) 230,
			(byte) 152, (byte) 217, (byte) 12, (byte) 189, (byte) 65,
			(byte) 85, (byte) 189, (byte) 204, (byte) 212, (byte) 161,
			(byte) 210, (byte) 217, (byte) 74, (byte) 75, (byte) 186,
			(byte) 122, (byte) 167, (byte) 149, (byte) 178, (byte) 202,
			(byte) 205, (byte) 246, (byte) 225, (byte) 225, (byte) 190,
			(byte) 56, (byte) 42, (byte) 162, (byte) 215, (byte) 107,
			(byte) 45, (byte) 121, (byte) 235, (byte) 195, (byte) 219,
			(byte) 22, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0,
			(byte) 5, (byte) 88, (byte) 46, (byte) 53, (byte) 48, (byte) 57,
			(byte) 0, (byte) 0, (byte) 2, (byte) 211, (byte) 48, (byte) 130,
			(byte) 2, (byte) 207, (byte) 48, (byte) 130, (byte) 1, (byte) 183,
			(byte) 160, (byte) 3, (byte) 2, (byte) 1, (byte) 2, (byte) 2,
			(byte) 4, (byte) 58, (byte) 247, (byte) 71, (byte) 185, (byte) 48,
			(byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) 134, (byte) 72,
			(byte) 134, (byte) 247, (byte) 13, (byte) 1, (byte) 1, (byte) 11,
			(byte) 5, (byte) 0, (byte) 48, (byte) 23, (byte) 49, (byte) 21,
			(byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85, (byte) 4,
			(byte) 3, (byte) 19, (byte) 12, (byte) 115, (byte) 101, (byte) 99,
			(byte) 117, (byte) 114, (byte) 101, (byte) 115, (byte) 111,
			(byte) 99, (byte) 107, (byte) 101, (byte) 116, (byte) 48,
			(byte) 32, (byte) 23, (byte) 13, (byte) 49, (byte) 52, (byte) 48,
			(byte) 53, (byte) 49, (byte) 48, (byte) 50, (byte) 48, (byte) 49,
			(byte) 56, (byte) 52, (byte) 48, (byte) 90, (byte) 24, (byte) 15,
			(byte) 50, (byte) 49, (byte) 49, (byte) 52, (byte) 48, (byte) 52,
			(byte) 49, (byte) 54, (byte) 50, (byte) 48, (byte) 49, (byte) 56,
			(byte) 52, (byte) 48, (byte) 90, (byte) 48, (byte) 23, (byte) 49,
			(byte) 21, (byte) 48, (byte) 19, (byte) 6, (byte) 3, (byte) 85,
			(byte) 4, (byte) 3, (byte) 19, (byte) 12, (byte) 115, (byte) 101,
			(byte) 99, (byte) 117, (byte) 114, (byte) 101, (byte) 115,
			(byte) 111, (byte) 99, (byte) 107, (byte) 101, (byte) 116,
			(byte) 48, (byte) 130, (byte) 1, (byte) 34, (byte) 48, (byte) 13,
			(byte) 6, (byte) 9, (byte) 42, (byte) 134, (byte) 72, (byte) 134,
			(byte) 247, (byte) 13, (byte) 1, (byte) 1, (byte) 1, (byte) 5,
			(byte) 0, (byte) 3, (byte) 130, (byte) 1, (byte) 15, (byte) 0,
			(byte) 48, (byte) 130, (byte) 1, (byte) 10, (byte) 2, (byte) 130,
			(byte) 1, (byte) 1, (byte) 0, (byte) 153, (byte) 113, (byte) 7,
			(byte) 44, (byte) 219, (byte) 76, (byte) 101, (byte) 226,
			(byte) 138, (byte) 96, (byte) 219, (byte) 60, (byte) 167,
			(byte) 138, (byte) 222, (byte) 6, (byte) 78, (byte) 169, (byte) 64,
			(byte) 188, (byte) 156, (byte) 190, (byte) 119, (byte) 16,
			(byte) 34, (byte) 228, (byte) 250, (byte) 253, (byte) 119,
			(byte) 75, (byte) 240, (byte) 60, (byte) 242, (byte) 52,
			(byte) 137, (byte) 146, (byte) 20, (byte) 130, (byte) 202,
			(byte) 226, (byte) 125, (byte) 19, (byte) 7, (byte) 34, (byte) 8,
			(byte) 61, (byte) 243, (byte) 202, (byte) 225, (byte) 206,
			(byte) 223, (byte) 53, (byte) 74, (byte) 56, (byte) 222, (byte) 47,
			(byte) 99, (byte) 235, (byte) 57, (byte) 73, (byte) 90, (byte) 198,
			(byte) 109, (byte) 104, (byte) 36, (byte) 255, (byte) 124,
			(byte) 57, (byte) 155, (byte) 248, (byte) 120, (byte) 56,
			(byte) 56, (byte) 38, (byte) 41, (byte) 216, (byte) 1, (byte) 216,
			(byte) 216, (byte) 100, (byte) 239, (byte) 79, (byte) 222,
			(byte) 34, (byte) 21, (byte) 182, (byte) 112, (byte) 136,
			(byte) 137, (byte) 16, (byte) 141, (byte) 15, (byte) 83, (byte) 94,
			(byte) 245, (byte) 36, (byte) 203, (byte) 178, (byte) 137,
			(byte) 159, (byte) 86, (byte) 220, (byte) 253, (byte) 112,
			(byte) 200, (byte) 50, (byte) 135, (byte) 215, (byte) 190,
			(byte) 21, (byte) 186, (byte) 84, (byte) 21, (byte) 96, (byte) 126,
			(byte) 253, (byte) 115, (byte) 209, (byte) 241, (byte) 94,
			(byte) 115, (byte) 219, (byte) 0, (byte) 25, (byte) 253,
			(byte) 209, (byte) 182, (byte) 118, (byte) 230, (byte) 10,
			(byte) 50, (byte) 131, (byte) 39, (byte) 249, (byte) 136,
			(byte) 11, (byte) 101, (byte) 192, (byte) 12, (byte) 210,
			(byte) 179, (byte) 237, (byte) 213, (byte) 68, (byte) 101,
			(byte) 58, (byte) 187, (byte) 255, (byte) 240, (byte) 164,
			(byte) 147, (byte) 72, (byte) 148, (byte) 227, (byte) 155,
			(byte) 88, (byte) 250, (byte) 101, (byte) 253, (byte) 87,
			(byte) 140, (byte) 168, (byte) 39, (byte) 163, (byte) 133,
			(byte) 150, (byte) 252, (byte) 226, (byte) 234, (byte) 52,
			(byte) 88, (byte) 40, (byte) 56, (byte) 23, (byte) 105, (byte) 236,
			(byte) 4, (byte) 113, (byte) 98, (byte) 4, (byte) 0, (byte) 117,
			(byte) 59, (byte) 77, (byte) 236, (byte) 135, (byte) 93, (byte) 54,
			(byte) 30, (byte) 6, (byte) 126, (byte) 90, (byte) 15, (byte) 105,
			(byte) 89, (byte) 216, (byte) 154, (byte) 72, (byte) 134,
			(byte) 209, (byte) 74, (byte) 197, (byte) 237, (byte) 51,
			(byte) 37, (byte) 33, (byte) 106, (byte) 50, (byte) 71, (byte) 134,
			(byte) 169, (byte) 173, (byte) 88, (byte) 111, (byte) 217,
			(byte) 117, (byte) 184, (byte) 97, (byte) 1, (byte) 38, (byte) 76,
			(byte) 112, (byte) 170, (byte) 190, (byte) 250, (byte) 96,
			(byte) 17, (byte) 45, (byte) 117, (byte) 183, (byte) 82,
			(byte) 155, (byte) 10, (byte) 53, (byte) 15, (byte) 214, (byte) 36,
			(byte) 134, (byte) 249, (byte) 146, (byte) 98, (byte) 99,
			(byte) 64, (byte) 158, (byte) 99, (byte) 227, (byte) 21, (byte) 92,
			(byte) 98, (byte) 90, (byte) 202, (byte) 214, (byte) 134,
			(byte) 233, (byte) 212, (byte) 149, (byte) 2, (byte) 3, (byte) 1,
			(byte) 0, (byte) 1, (byte) 163, (byte) 33, (byte) 48, (byte) 31,
			(byte) 48, (byte) 29, (byte) 6, (byte) 3, (byte) 85, (byte) 29,
			(byte) 14, (byte) 4, (byte) 22, (byte) 4, (byte) 20, (byte) 115,
			(byte) 110, (byte) 177, (byte) 165, (byte) 41, (byte) 26,
			(byte) 142, (byte) 198, (byte) 221, (byte) 63, (byte) 79,
			(byte) 252, (byte) 219, (byte) 159, (byte) 68, (byte) 102,
			(byte) 76, (byte) 153, (byte) 128, (byte) 164, (byte) 48,
			(byte) 13, (byte) 6, (byte) 9, (byte) 42, (byte) 134, (byte) 72,
			(byte) 134, (byte) 247, (byte) 13, (byte) 1, (byte) 1, (byte) 11,
			(byte) 5, (byte) 0, (byte) 3, (byte) 130, (byte) 1, (byte) 1,
			(byte) 0, (byte) 118, (byte) 55, (byte) 245, (byte) 122,
			(byte) 159, (byte) 155, (byte) 98, (byte) 122, (byte) 229,
			(byte) 186, (byte) 23, (byte) 207, (byte) 109, (byte) 225,
			(byte) 220, (byte) 74, (byte) 51, (byte) 218, (byte) 10,
			(byte) 115, (byte) 137, (byte) 103, (byte) 127, (byte) 28,
			(byte) 30, (byte) 184, (byte) 149, (byte) 249, (byte) 193,
			(byte) 206, (byte) 208, (byte) 181, (byte) 191, (byte) 128,
			(byte) 18, (byte) 208, (byte) 24, (byte) 132, (byte) 147,
			(byte) 184, (byte) 198, (byte) 82, (byte) 204, (byte) 183,
			(byte) 127, (byte) 87, (byte) 234, (byte) 136, (byte) 197,
			(byte) 34, (byte) 232, (byte) 124, (byte) 210, (byte) 2,
			(byte) 192, (byte) 69, (byte) 246, (byte) 25, (byte) 232,
			(byte) 162, (byte) 0, (byte) 157, (byte) 216, (byte) 194,
			(byte) 26, (byte) 207, (byte) 225, (byte) 169, (byte) 59,
			(byte) 246, (byte) 52, (byte) 51, (byte) 150, (byte) 210,
			(byte) 50, (byte) 118, (byte) 58, (byte) 154, (byte) 45,
			(byte) 128, (byte) 138, (byte) 47, (byte) 174, (byte) 83,
			(byte) 117, (byte) 18, (byte) 224, (byte) 9, (byte) 146,
			(byte) 180, (byte) 178, (byte) 22, (byte) 76, (byte) 82,
			(byte) 229, (byte) 16, (byte) 150, (byte) 127, (byte) 13,
			(byte) 122, (byte) 218, (byte) 159, (byte) 195, (byte) 232,
			(byte) 168, (byte) 206, (byte) 105, (byte) 82, (byte) 37,
			(byte) 252, (byte) 186, (byte) 223, (byte) 222, (byte) 7,
			(byte) 106, (byte) 87, (byte) 218, (byte) 89, (byte) 22,
			(byte) 252, (byte) 7, (byte) 177, (byte) 52, (byte) 180, (byte) 9,
			(byte) 16, (byte) 29, (byte) 57, (byte) 192, (byte) 209,
			(byte) 225, (byte) 155, (byte) 16, (byte) 219, (byte) 38,
			(byte) 90, (byte) 174, (byte) 152, (byte) 140, (byte) 252,
			(byte) 114, (byte) 133, (byte) 106, (byte) 24, (byte) 107,
			(byte) 227, (byte) 80, (byte) 166, (byte) 63, (byte) 47, (byte) 16,
			(byte) 15, (byte) 89, (byte) 242, (byte) 19, (byte) 87, (byte) 193,
			(byte) 250, (byte) 222, (byte) 223, (byte) 183, (byte) 61,
			(byte) 91, (byte) 17, (byte) 92, (byte) 35, (byte) 142, (byte) 44,
			(byte) 153, (byte) 135, (byte) 86, (byte) 97, (byte) 70,
			(byte) 205, (byte) 38, (byte) 192, (byte) 18, (byte) 244,
			(byte) 61, (byte) 46, (byte) 21, (byte) 145, (byte) 99, (byte) 72,
			(byte) 142, (byte) 37, (byte) 19, (byte) 219, (byte) 167,
			(byte) 62, (byte) 71, (byte) 197, (byte) 86, (byte) 152,
			(byte) 139, (byte) 122, (byte) 231, (byte) 122, (byte) 206,
			(byte) 42, (byte) 142, (byte) 164, (byte) 237, (byte) 19,
			(byte) 60, (byte) 95, (byte) 239, (byte) 191, (byte) 64,
			(byte) 188, (byte) 94, (byte) 154, (byte) 199, (byte) 252,
			(byte) 62, (byte) 26, (byte) 181, (byte) 194, (byte) 141,
			(byte) 13, (byte) 1, (byte) 112, (byte) 161, (byte) 195,
			(byte) 149, (byte) 116, (byte) 57, (byte) 118, (byte) 114,
			(byte) 248, (byte) 235, (byte) 54, (byte) 229, (byte) 48,
			(byte) 53, (byte) 30, (byte) 145, (byte) 199, (byte) 207,
			(byte) 49, (byte) 175, (byte) 44, (byte) 172, (byte) 120,
			(byte) 254, (byte) 181, (byte) 100, (byte) 113, (byte) 191,
			(byte) 64, (byte) 131, (byte) 125, (byte) 80, (byte) 180,
			(byte) 229, (byte) 109, (byte) 97, (byte) 8, (byte) 166,
			(byte) 155, (byte) 72, (byte) 252, (byte) 84, (byte) 62, (byte) 97,
			(byte) 80, (byte) 26, (byte) 17, (byte) 143, (byte) 96, (byte) 16,
			(byte) 204, (byte) 86, (byte) 61, (byte) 226, (byte) 149 };

	
	public static KeyStore getKeyStore()
	{
		KeyStore ks = null;
		try{
		ks = KeyStore.getInstance("JKS");
        ks.load(asInputStream(), getKeyStorePassword());
		}catch(Exception ex){
			throw new RuntimeException("Failed to load SSL key store.", ex);
		}
        return ks;
	}
	
	public static InputStream asInputStream() {		
		return new ByteArrayInputStream(CERT_BYTES);
	}

	public static char[] getCertificatePassword() {
		return "inc0rrect".toCharArray();
	}

	public static char[] getKeyStorePassword() {
		return "mu$tch8ng3".toCharArray();
	}
	
	public static String getCertificatePasswordString() {
		return "inc0rrect";
	}

	public static String getKeyStorePasswordString() {
		return "mu$tch8ng3";
	}

	private SecureSocketKeyStore() {

	}

}
