package e.alexk.mynote;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eclipsesource.v8.V8;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * Created by administrator on 16.03.2018.
 */

/*
class Crypto {
    private final static String HEX = "0123456789ABCDEF";

    public static String encrypt(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("RSA/None/PKCS1Padding");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr);
        // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}*/

public class NoteActivity extends AppCompatActivity {
    // Инициализируем компонент
    EditText ViewNoteText;
    EditText ViewEncryptPass;
    TextView ViewCreationDate;
    TextView ViewTillDeadline;
    TextView ViewDeadlineDate;
    Button DecryptButton;
    Button DeleteButton;
    CheckBox is_encrypted;

    Boolean bool_is_encrypted;

    String Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ViewNoteText = findViewById(R.id.note_text);
        ViewCreationDate = findViewById(R.id.Create_date_text);
        ViewTillDeadline = findViewById(R.id.textViewTillDeadline);
        ViewDeadlineDate = findViewById(R.id.deadline_date);
        is_encrypted = findViewById(R.id.checkBoxCrypt);
        ViewEncryptPass = findViewById(R.id.editTextDecryptPass);
        DecryptButton = findViewById(R.id.buttonDecrypt);
        DeleteButton = findViewById(R.id.buttonDelete);
        // задаём текст
        String temp = getIntent().getStringExtra("note_text");
        ViewNoteText.setText(temp);

        // задаём текст
        ViewCreationDate.setText(getIntent().getStringExtra("note_creation_string"));

        // задаём текст
        String note_deadline_string = getIntent().getStringExtra("note_deadline_string");
        ViewTillDeadline.setText(note_deadline_string);

        String note_deadline_date_string = getIntent().getStringExtra("note_deadline_date_string");
        ViewDeadlineDate.setText(note_deadline_date_string);

        DecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewEncryptPass.getText().length() != 0) {
                    //UserNote.setText("Все ок)");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "oK!", Toast.LENGTH_SHORT);
                    toast.show();
                    // String res=encrypt("Hello world","111");
                    // String res2=decrypt(res,"111");
                    //System.out.println("Decrypted: " + res2);

                    String note = ViewNoteText.getText().toString();
                    String pass = ViewEncryptPass.getText().toString();
                    String res3 = decrypt(note, pass);
                    if (!res3.equals(""))
                        ViewNoteText.setText(res3);
                    else {
                        Toast toast1 = Toast.makeText(getApplicationContext(),
                                "Wrong pass!", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Empty pass!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Id = getIntent().getStringExtra("Id");
        if (note_deadline_string.equals("")) {
            ViewTillDeadline.setVisibility(View.GONE);
            ViewDeadlineDate.setVisibility(View.GONE);
        }
        bool_is_encrypted = getIntent().getBooleanExtra("is_encrypted", false);
        is_encrypted.setChecked(bool_is_encrypted);
        if (!bool_is_encrypted) {
            is_encrypted.setVisibility(View.GONE);
            ViewEncryptPass.setVisibility(View.GONE);
            DecryptButton.setVisibility(View.GONE);
        }


    }

    public static void JS(String text, String key) {
        V8 runtime = V8.createV8Runtime();
        runtime.executeVoidScript("var CryptoJS = CryptoJS || (function(Math, undefined) { var C = {}; var C_lib = C.lib = {}; var Base = C_lib.Base = (function() { function F() {} return { extend: function(overrides) { F.prototype = this; var subtype = new F(); if (overrides) { subtype.mixIn(overrides); } if (!subtype.hasOwnProperty('init')) { subtype.init = function() { subtype.$super.init.apply(this, arguments); }; } subtype.init.prototype = subtype; subtype.$super = this; return subtype; }, create: function() { var instance = this.extend(); instance.init.apply(instance, arguments); return instance; }, init: function() {}, mixIn: function(properties) { for (var propertyName in properties) { if (properties.hasOwnProperty(propertyName)) { this[propertyName] = properties[propertyName]; } } if (properties.hasOwnProperty('toString')) { this.toString = properties.toString; } }, clone: function() { return this.init.prototype.extend(this); } }; }()); var WordArray = C_lib.WordArray = Base.extend({ init: function(words, sigBytes) { words = this.words = words || []; if (sigBytes != undefined) { this.sigBytes = sigBytes; } else { this.sigBytes = words.length * 4; } }, toString: function(encoder) { return (encoder || Hex) .stringify(this); }, concat: function(wordArray) { var thisWords = this.words; var thatWords = wordArray.words; var thisSigBytes = this.sigBytes; var thatSigBytes = wordArray.sigBytes; this.clamp(); if (thisSigBytes % 4) { for (var i = 0; i < thatSigBytes; i++) { var thatByte = (thatWords[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; thisWords[(thisSigBytes + i) >>> 2] |= thatByte << (24 - ((thisSigBytes + i) % 4) * 8); } } else if (thatWords.length > 0xffff) { for (var i = 0; i < thatSigBytes; i += 4) { thisWords[(thisSigBytes + i) >>> 2] = thatWords[i >>> 2]; } } else { thisWords.push.apply(thisWords, thatWords); } this.sigBytes += thatSigBytes; return this; }, clamp: function() { var words = this.words; var sigBytes = this.sigBytes; words[sigBytes >>> 2] &= 0xffffffff << (32 - (sigBytes % 4) * 8); words.length = Math.ceil(sigBytes / 4); }, /** * Creates a copy of this word array. * * @return {WordArray} The clone. * * @example * * var clone = wordArray.clone(); */ clone: function() { var clone = Base.clone.call(this); clone.words = this.words.slice(0); return clone; }, /** * Creates a word array filled with random bytes. * * @param {number} nBytes The number of random bytes to generate. * * @return {WordArray} The random word array. * * @static * * @example * * var wordArray = CryptoJS.lib.WordArray.random(16); */ random: function(nBytes) { var words = []; for (var i = 0; i < nBytes; i += 4) { words.push((Math.random() * 0x100000000) | 0); } return new WordArray.init(words, nBytes); } }); /** * Encoder namespace. */ var C_enc = C.enc = {}; /** * Hex encoding strategy. */ var Hex = C_enc.Hex = { /** * Converts a word array to a hex string. * * @param {WordArray} wordArray The word array. * * @return {string} The hex string. * * @static * * @example * * var hexString = CryptoJS.enc.Hex.stringify(wordArray); */ stringify: function(wordArray) { // Shortcuts var words = wordArray.words; var sigBytes = wordArray.sigBytes; // Convert var hexChars = []; for (var i = 0; i < sigBytes; i++) { var bite = (words[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; hexChars.push((bite >>> 4) .toString(16)); hexChars.push((bite & 0x0f) .toString(16)); } return hexChars.join(''); }, /** * Converts a hex string to a word array. * * @param {string} hexStr The hex string. * * @return {WordArray} The word array. * * @static * * @example * * var wordArray = CryptoJS.enc.Hex.parse(hexString); */ parse: function(hexStr) { // Shortcut var hexStrLength = hexStr.length; // Convert var words = []; for (var i = 0; i < hexStrLength; i += 2) { words[i >>> 3] |= parseInt(hexStr.substr(i, 2), 16) << (24 - (i % 8) * 4); } return new WordArray.init(words, hexStrLength / 2); } }; var Latin1 = C_enc.Latin1 = { stringify: function(wordArray) { var words = wordArray.words; var sigBytes = wordArray.sigBytes; var latin1Chars = []; for (var i = 0; i < sigBytes; i++) { var bite = (words[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; latin1Chars.push(String.fromCharCode(bite)); } return latin1Chars.join(''); }, parse: function(latin1Str) { var latin1StrLength = latin1Str.length; var words = []; for (var i = 0; i < latin1StrLength; i++) { words[i >>> 2] |= (latin1Str.charCodeAt(i) & 0xff) << (24 - (i % 4) * 8); } return new WordArray.init(words, latin1StrLength); } }; var Utf8 = C_enc.Utf8 = { stringify: function(wordArray) { try { return decodeURIComponent(escape(Latin1.stringify(wordArray))); } catch (e) { throw new Error('Malformed UTF-8 data'); } }, parse: function(utf8Str) { return Latin1.parse(unescape(encodeURIComponent(utf8Str))); } }; var BufferedBlockAlgorithm = C_lib.BufferedBlockAlgorithm = Base.extend({ reset: function() { this._data = new WordArray.init(); this._nDataBytes = 0; }, _append: function(data) { if (typeof data == 'string') { data = Utf8.parse(data); } this._data.concat(data); this._nDataBytes += data.sigBytes; }, _process: function(doFlush) { var data = this._data; var dataWords = data.words; var dataSigBytes = data.sigBytes; var blockSize = this.blockSize; var blockSizeBytes = blockSize * 4; var nBlocksReady = dataSigBytes / blockSizeBytes; if (doFlush) { // Round up to include partial blocks nBlocksReady = Math.ceil(nBlocksReady); } else { // Round down to include only full blocks, // less the number of blocks that must remain in the buffer nBlocksReady = Math.max((nBlocksReady | 0) - this._minBufferSize, 0); } // Count words ready var nWordsReady = nBlocksReady * blockSize; // Count bytes ready var nBytesReady = Math.min(nWordsReady * 4, dataSigBytes); // Process blocks if (nWordsReady) { for (var offset = 0; offset < nWordsReady; offset += blockSize) { // Perform concrete-algorithm logic this._doProcessBlock(dataWords, offset); } // Remove processed words var processedWords = dataWords.splice(0, nWordsReady); data.sigBytes -= nBytesReady; } // Return processed words return new WordArray.init(processedWords, nBytesReady); }, /** * Creates a copy of this object. * * @return {Object} The clone. * * @example * * var clone = bufferedBlockAlgorithm.clone(); */ clone: function() { var clone = Base.clone.call(this); clone._data = this._data.clone(); return clone; }, _minBufferSize: 0 }); /** * Abstract hasher template. * * @property {number} blockSize The number of 32-bit words this hasher operates on. Default: 16 (512 bits) */ var Hasher = C_lib.Hasher = BufferedBlockAlgorithm.extend({ /** * Configuration options. */ cfg: Base.extend(), /** * Initializes a newly created hasher. * * @param {Object} cfg (Optional) The configuration options to use for this hash computation. * * @example * * var hasher = CryptoJS.algo.SHA256.create(); */ init: function(cfg) { // Apply config defaults this.cfg = this.cfg.extend(cfg); // Set initial values this.reset(); }, /** * Resets this hasher to its initial state. * * @example * * hasher.reset(); */ reset: function() { // Reset data buffer BufferedBlockAlgorithm.reset.call(this); // Perform concrete-hasher logic this._doReset(); }, /** * Updates this hasher with a message. * * @param {WordArray|string} messageUpdate The message to append. * * @return {Hasher} This hasher. * * @example * * hasher.update('message'); * hasher.update(wordArray); */ update: function(messageUpdate) { // Append this._append(messageUpdate); // Update the hash this._process(); // Chainable return this; }, /** * Finalizes the hash computation. * Note that the finalize operation is effectively a destructive, read-once operation. * * @param {WordArray|string} messageUpdate (Optional) A final message update. * * @return {WordArray} The hash. * * @example * * var hash = hasher.finalize(); * var hash = hasher.finalize('message'); * var hash = hasher.finalize(wordArray); */ finalize: function(messageUpdate) { // Final message update if (messageUpdate) { this._append(messageUpdate); } // Perform concrete-hasher logic var hash = this._doFinalize(); return hash; }, blockSize: 512 / 32, _createHelper: function(hasher) { return function(message, cfg) { return new hasher.init(cfg) .finalize(message); }; }, _createHmacHelper: function(hasher) { return function(message, key) { return new C_algo.HMAC.init(hasher, key) .finalize(message); }; } }); var C_algo = C.algo = {}; return C; }(Math));"
        );
        // "(function() { var C = CryptoJS; var C_lib = C.lib; var WordArray = C_lib.WordArray; var C_enc = C.enc; var Base64 = C_enc.Base64 = { stringify: function(wordArray) { var words = wordArray.words; var sigBytes = wordArray.sigBytes; var map = this._map; wordArray.clamp(); var base64Chars = []; for (var i = 0; i < sigBytes; i += 3) { var byte1 = (words[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; var byte2 = (words[(i + 1) >>> 2] >>> (24 - ((i + 1) % 4) * 8)) & 0xff; var byte3 = (words[(i + 2) >>> 2] >>> (24 - ((i + 2) % 4) * 8)) & 0xff; var triplet = (byte1 << 16) | (byte2 << 8) | byte3; for (var j = 0; (j < 4) && (i + j * 0.75 < sigBytes); j++) { base64Chars.push(map.charAt((triplet >>> (6 * (3 - j))) & 0x3f)); } } var paddingChar = map.charAt(64); if (paddingChar) { while (base64Chars.length % 4) { base64Chars.push(paddingChar); } } return base64Chars.join(''); }, parse: function(base64Str) { var base64StrLength = base64Str.length; var map = this._map; var paddingChar = map.charAt(64); if (paddingChar) { var paddingIndex = base64Str.indexOf(paddingChar); if (paddingIndex != -1) { base64StrLength = paddingIndex; } } var words = []; var nBytes = 0; for (var i = 0; i < base64StrLength; i++) { if (i % 4) { var bits1 = map.indexOf(base64Str.charAt(i - 1)) << ((i % 4) * 2); var bits2 = map.indexOf(base64Str.charAt(i)) >>> (6 - (i % 4) * 2); words[nBytes >>> 2] |= (bits1 | bits2) << (24 - (nBytes % 4) * 8); nBytes++; } } return WordArray.create(words, nBytes); }, _map: 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=' }; }()); var CryptoJS = CryptoJS || (function(Math, undefined) { var C = {}; var C_lib = C.lib = {}; var Base = C_lib.Base = (function() { function F() {} return { extend: function(overrides) { F.prototype = this; var subtype = new F(); if (overrides) { subtype.mixIn(overrides); } if (!subtype.hasOwnProperty('init')) { subtype.init = function() { subtype.$super.init.apply(this, arguments); }; } subtype.init.prototype = subtype; subtype.$super = this; return subtype; }, create: function() { var instance = this.extend(); instance.init.apply(instance, arguments); a return instance; }, init: function() {}, mixIn: function(properties) { for (var propertyName in properties) { if (properties.hasOwnProperty(propertyName)) { this[propertyName] = properties[propertyName]; } } if (properties.hasOwnProperty('toString')) { this.toString = properties.toString; } }, clone: function() { return this.init.prototype.extend(this); } }; }()); var WordArray = C_lib.WordArray = Base.extend({ init: function(words, sigBytes) { words = this.words = words || []; if (sigBytes != undefined) { this.sigBytes = sigBytes; } else { this.sigBytes = words.length * 4; } }, toString: function(encoder) { return (encoder || Hex) .stringify(this); }, concat: function(wordArray) { var thisWords = this.words; var thatWords = wordArray.words; var thisSigBytes = this.sigBytes; var thatSigBytes = wordArray.sigBytes; this.clamp(); if (thisSigBytes % 4) { for (var i = 0; i < thatSigBytes; i++) { var thatByte = (thatWords[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; thisWords[(thisSigBytes + i) >>> 2] |= thatByte << (24 - ((thisSigBytes + i) % 4) * 8); } } else if (thatWords.length > 0xffff) { for (var i = 0; i < thatSigBytes; i += 4) { thisWords[(thisSigBytes + i) >>> 2] = thatWords[i >>> 2]; } } else { thisWords.push.apply(thisWords, thatWords); } this.sigBytes += thatSigBytes; return this; }, clamp: function() { var words = this.words; var sigBytes = this.sigBytes; words[sigBytes >>> 2] &= 0xffffffff << (32 - (sigBytes % 4) * 8); words.length = Math.ceil(sigBytes / 4); }, /** * Creates a copy of this word array. * * @return {WordArray} The clone. * * @example * * var clone = wordArray.clone(); */ clone: function() { var clone = Base.clone.call(this); clone.words = this.words.slice(0); return clone; }, /** * Creates a word array filled with random bytes. * * @param {number} nBytes The number of random bytes to generate. * * @return {WordArray} The random word array. * * @static * * @example * * var wordArray = CryptoJS.lib.WordArray.random(16); */ random: function(nBytes) { var words = []; for (var i = 0; i < nBytes; i += 4) { words.push((Math.random() * 0x100000000) | 0); } return new WordArray.init(words, nBytes); } }); /** * Encoder namespace. */ var C_enc = C.enc = {}; /** * Hex encoding strategy. */ var Hex = C_enc.Hex = { /** * Converts a word array to a hex string. * * @param {WordArray} wordArray The word array. * * @return {string} The hex string. * * @static * * @example * * var hexString = CryptoJS.enc.Hex.stringify(wordArray); */ stringify: function(wordArray) { // Shortcuts var words = wordArray.words; var sigBytes = wordArray.sigBytes; // Convert var hexChars = []; for (var i = 0; i < sigBytes; i++) { var bite = (words[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; hexChars.push((bite >>> 4) .toString(16)); hexChars.push((bite & 0x0f) .toString(16)); } return hexChars.join(''); }, /** * Converts a hex string to a word array. * * @param {string} hexStr The hex string. * * @return {WordArray} The word array. * * @static * * @example * * var wordArray = CryptoJS.enc.Hex.parse(hexString); */ parse: function(hexStr) { // Shortcut var hexStrLength = hexStr.length; // Convert var words = []; for (var i = 0; i < hexStrLength; i += 2) { words[i >>> 3] |= parseInt(hexStr.substr(i, 2), 16) << (24 - (i % 8) * 4); } return new WordArray.init(words, hexStrLength / 2); } }; var Latin1 = C_enc.Latin1 = { stringify: function(wordArray) { var words = wordArray.words; var sigBytes = wordArray.sigBytes; var latin1Chars = []; for (var i = 0; i < sigBytes; i++) { var bite = (words[i >>> 2] >>> (24 - (i % 4) * 8)) & 0xff; latin1Chars.push(String.fromCharCode(bite)); } return latin1Chars.join(''); }, parse: function(latin1Str) { var latin1StrLength = latin1Str.length; var words = []; for (var i = 0; i < latin1StrLength; i++) { words[i >>> 2] |= (latin1Str.charCodeAt(i) & 0xff) << (24 - (i % 4) * 8); } return new WordArray.init(words, latin1StrLength); } }; var Utf8 = C_enc.Utf8 = { stringify: function(wordArray) { try { return decodeURIComponent(escape(Latin1.stringify(wordArray))); } catch (e) { throw new Error('Malformed UTF-8 data'); } }, parse: function(utf8Str) { return Latin1.parse(unescape(encodeURIComponent(utf8Str))); } }; var BufferedBlockAlgorithm = C_lib.BufferedBlockAlgorithm = Base.extend({ reset: function() { this._data = new WordArray.init(); this._nDataBytes = 0; }, _append: function(data) { if (typeof data == 'string') { data = Utf8.parse(data); } this._data.concat(data); this._nDataBytes += data.sigBytes; }, _process: function(doFlush) { var data = this._data; var dataWords = data.words; var dataSigBytes = data.sigBytes; var blockSize = this.blockSize; var blockSizeBytes = blockSize * 4; var nBlocksReady = dataSigBytes / blockSizeBytes; if (doFlush) { // Round up to include partial blocks nBlocksReady = Math.ceil(nBlocksReady); } else { // Round down to include only full blocks, // less the number of blocks that must remain in the buffer nBlocksReady = Math.max((nBlocksReady | 0) - this._minBufferSize, 0); } // Count words ready var nWordsReady = nBlocksReady * blockSize; // Count bytes ready var nBytesReady = Math.min(nWordsReady * 4, dataSigBytes); // Process blocks if (nWordsReady) { for (var offset = 0; offset < nWordsReady; offset += blockSize) { // Perform concrete-algorithm logic this._doProcessBlock(dataWords, offset); } // Remove processed words var processedWords = dataWords.splice(0, nWordsReady); data.sigBytes -= nBytesReady; } // Return processed words return new WordArray.init(processedWords, nBytesReady); }, /** * Creates a copy of this object. * * @return {Object} The clone. * * @example * * var clone = bufferedBlockAlgorithm.clone(); */ clone: function() { var clone = Base.clone.call(this); clone._data = this._data.clone(); return clone; }, _minBufferSize: 0 }); /** * Abstract hasher template. * * @property {number} blockSize The number of 32-bit words this hasher operates on. Default: 16 (512 bits) */ var Hasher = C_lib.Hasher = BufferedBlockAlgorithm.extend({ /** * Configuration options. */ cfg: Base.extend(), /** * Initializes a newly created hasher. * * @param {Object} cfg (Optional) The configuration options to use for this hash computation. * * @example * * var hasher = CryptoJS.algo.SHA256.create(); */ init: function(cfg) { // Apply config defaults this.cfg = this.cfg.extend(cfg); // Set initial values this.reset(); }, /** * Resets this hasher to its initial state. * * @example * * hasher.reset(); */ reset: function() { // Reset data buffer BufferedBlockAlgorithm.reset.call(this); // Perform concrete-hasher logic this._doReset(); }, /** * Updates this hasher with a message. * * @param {WordArray|string} messageUpdate The message to append. * * @return {Hasher} This hasher. * * @example * * hasher.update('message'); * hasher.update(wordArray); */ update: function(messageUpdate) { // Append this._append(messageUpdate); // Update the hash this._process(); // Chainable return this; }, /** * Finalizes the hash computation. * Note that the finalize operation is effectively a destructive, read-once operation. * * @param {WordArray|string} messageUpdate (Optional) A final message update. * * @return {WordArray} The hash. * * @example * * var hash = hasher.finalize(); * var hash = hasher.finalize('message'); * var hash = hasher.finalize(wordArray); */ finalize: function(messageUpdate) { // Final message update if (messageUpdate) { this._append(messageUpdate); } // Perform concrete-hasher logic var hash = this._doFinalize(); return hash; }, blockSize: 512 / 32, _createHelper: function(hasher) { return function(message, cfg) { return new hasher.init(cfg) .finalize(message); }; }, _createHmacHelper: function(hasher) { return function(message, key) { return new C_algo.HMAC.init(hasher, key) .finalize(message); }; } }); var C_algo = C.algo = {}; return C; }(Math));" +
        // "var CryptoJS = CryptoJS || function(u, l) { var d = {}, n = d.lib = {}, p = function() {}, s = n.Base = { extend: function(a) { p.prototype = this; var c = new p; a && c.mixIn(a); c.hasOwnProperty(\"init\") || (c.init = function() { c.$super.init.apply(this, arguments) }); c.init.prototype = c; c.$super = this; return c }, create: function() { var a = this.extend(); a.init.apply(a, arguments); return a }, init: function() {}, mixIn: function(a) { for (var c in a) a.hasOwnProperty(c) && (this[c] = a[c]); a.hasOwnProperty(\"toString\") && (this.toString = a.toString) }, clone: function() { return this.init.prototype.extend(this) } }, q = n.WordArray = s.extend({ init: function(a, c) { a = this.words = a || []; this.sigBytes = c != l ? c : 4 * a.length }, toString: function(a) { return (a || v) .stringify(this) }, concat: function(a) { var c = this.words, m = a.words, f = this.sigBytes; a = a.sigBytes; this.clamp(); if (f % 4) for (var t = 0; t < a; t++) c[f + t >>> 2] |= (m[t >>> 2] >>> 24 - 8 * (t % 4) & 255) << 24 - 8 * ((f + t) % 4); else if (65535 < m.length) for (t = 0; t < a; t += 4) c[f + t >>> 2] = m[t >>> 2]; else c.push.apply(c, m); this.sigBytes += a; return this }, clamp: function() { var a = this.words, c = this.sigBytes; a[c >>> 2] &= 4294967295 << 32 - 8 * (c % 4); a.length = u.ceil(c / 4) }, clone: function() { var a = s.clone.call(this); a.words = this.words.slice(0); return a }, random: function(a) { for (var c = [], m = 0; m < a; m += 4) c.push(4294967296 * u.random() | 0); return new q.init(c, a) } }), w = d.enc = {}, v = w.Hex = { stringify: function(a) { var c = a.words; a = a.sigBytes; for (var m = [], f = 0; f < a; f++) { var t = c[f >>> 2] >>> 24 - 8 * (f % 4) & 255; m.push((t >>> 4) .toString(16)); m.push((t & 15) .toString(16)) } return m.join(\"\") }, parse: function(a) { for (var c = a.length, m = [], f = 0; f < c; f += 2) m[f >>> 3] |= parseInt(a.substr(f, 2), 16) << 24 - 4 * (f % 8); return new q.init(m, c / 2) } }, b = w.Latin1 = { stringify: function(a) { var c = a.words; a = a.sigBytes; for (var m = [], f = 0; f < a; f++) m.push(String.fromCharCode(c[f >>> 2] >>> 24 - 8 * (f % 4) & 255)); return m.join(\"\") }, parse: function(a) { for (var c = a.length, m = [], f = 0; f < c; f++) m[f >>> 2] |= (a.charCodeAt(f) & 255) << 24 - 8 * (f % 4); return new q.init(m, c) } }, x = w.Utf8 = { stringify: function(a) { try { return decodeURIComponent(escape(b.stringify(a))) } catch (c) { throw Error(\"Malformed UTF-8 data\"); } }, parse: function(a) { return b.parse(unescape(encodeURIComponent(a))) } }, r = n.BufferedBlockAlgorithm = s.extend({ reset: function() { this._data = new q.init; this._nDataBytes = 0 }, _append: function(a) { \"string\" == typeof a && (a = x.parse(a)); this._data.concat(a); this._nDataBytes += a.sigBytes }, _process: function(a) { var c = this._data, m = c.words, f = c.sigBytes, t = this.blockSize, b = f / (4 * t), b = a ? u.ceil(b) : u.max((b | 0) - this._minBufferSize, 0); a = b * t; f = u.min(4 * a, f); if (a) { for (var e = 0; e < a; e += t) this._doProcessBlock(m, e); e = m.splice(0, a); c.sigBytes -= f } return new q.init(e, f) }, clone: function() { var a = s.clone.call(this); a._data = this._data.clone(); return a }, _minBufferSize: 0 }); n.Hasher = r.extend({ cfg: s.extend(), init: function(a) { this.cfg = this.cfg.extend(a); this.reset() }, reset: function() { r.reset.call(this); this._doReset() }, update: function(a) { this._append(a); this._process(); return this }, finalize: function(a) { a && this._append(a); return this._doFinalize() }, blockSize: 16, _createHelper: function(a) { return function(c, m) { return (new a.init(m)) .finalize(c) } }, _createHmacHelper: function(a) { return function(c, m) { return (new e.HMAC.init(a, m)) .finalize(c) } } }); var e = d.algo = {}; return d }(Math); (function() { var u = CryptoJS, l = u.lib.WordArray; u.enc.Base64 = { stringify: function(d) { var n = d.words, l = d.sigBytes, s = this._map; d.clamp(); d = []; for (var q = 0; q < l; q += 3) for (var w = (n[q >>> 2] >>> 24 - 8 * (q % 4) & 255) << 16 | (n[q + 1 >>> 2] >>> 24 - 8 * ((q + 1) % 4) & 255) << 8 | n[q + 2 >>> 2] >>> 24 - 8 * ((q + 2) % 4) & 255, v = 0; 4 > v && q + 0.75 * v < l; v++) d.push(s.charAt(w >>> 6 * (3 - v) & 63)); if (n = s.charAt(64)) for (; d.length % 4;) d.push(n); return d.join(\"\") }, parse: function(d) { var n = d.length, p = this._map, s = p.charAt(64); s && (s = d.indexOf(s), -1 != s && (n = s)); for (var s = [], q = 0, w = 0; w < n; w++) if (w % 4) { var v = p.indexOf(d.charAt(w - 1)) << 2 * (w % 4), b = p.indexOf(d.charAt(w)) >>> 6 - 2 * (w % 4); s[q >>> 2] |= (v | b) << 24 - 8 * (q % 4); q++ } return l.create(s, q) }, _map: \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\" } })(); (function(u) { function l(b, e, a, c, m, f, t) { b = b + (e & a | ~e & c) + m + t; return (b << f | b >>> 32 - f) + e } function d(b, e, a, c, m, f, t) { b = b + (e & c | a & ~c) + m + t; return (b << f | b >>> 32 - f) + e } function n(b, e, a, c, m, f, t) { b = b + (e ^ a ^ c) + m + t; return (b << f | b >>> 32 - f) + e } function p(b, e, a, c, m, f, t) { b = b + (a ^ (e | ~c)) + m + t; return (b << f | b >>> 32 - f) + e } for (var s = CryptoJS, q = s.lib, w = q.WordArray, v = q.Hasher, q = s.algo, b = [], x = 0; 64 > x; x++) b[x] = 4294967296 * u.abs(u.sin(x + 1)) | 0; q = q.MD5 = v.extend({ _doReset: function() { this._hash = new w.init([1732584193, 4023233417, 2562383102, 271733878]) }, _doProcessBlock: function(r, e) { for (var a = 0; 16 > a; a++) { var c = e + a, m = r[c]; r[c] = (m << 8 | m >>> 24) & 16711935 | (m << 24 | m >>> 8) & 4278255360 } var a = this._hash.words, c = r[e + 0], m = r[e + 1], f = r[e + 2], t = r[e + 3], y = r[e + 4], q = r[e + 5], s = r[e + 6], w = r[e + 7], v = r[e + 8], u = r[e + 9], x = r[e + 10], z = r[e + 11], A = r[e + 12], B = r[e + 13], C = r[e + 14], D = r[e + 15], g = a[0], h = a[1], j = a[2], k = a[3], g = l(g, h, j, k, c, 7, b[0]), k = l(k, g, h, j, m, 12, b[1]), j = l(j, k, g, h, f, 17, b[2]), h = l(h, j, k, g, t, 22, b[3]), g = l(g, h, j, k, y, 7, b[4]), k = l(k, g, h, j, q, 12, b[5]), j = l(j, k, g, h, s, 17, b[6]), h = l(h, j, k, g, w, 22, b[7]), g = l(g, h, j, k, v, 7, b[8]), k = l(k, g, h, j, u, 12, b[9]), j = l(j, k, g, h, x, 17, b[10]), h = l(h, j, k, g, z, 22, b[11]), g = l(g, h, j, k, A, 7, b[12]), k = l(k, g, h, j, B, 12, b[13]), j = l(j, k, g, h, C, 17, b[14]), h = l(h, j, k, g, D, 22, b[15]), g = d(g, h, j, k, m, 5, b[16]), k = d(k, g, h, j, s, 9, b[17]), j = d(j, k, g, h, z, 14, b[18]), h = d(h, j, k, g, c, 20, b[19]), g = d(g, h, j, k, q, 5, b[20]), k = d(k, g, h, j, x, 9, b[21]), j = d(j, k, g, h, D, 14, b[22]), h = d(h, j, k, g, y, 20, b[23]), g = d(g, h, j, k, u, 5, b[24]), k = d(k, g, h, j, C, 9, b[25]), j = d(j, k, g, h, t, 14, b[26]), h = d(h, j, k, g, v, 20, b[27]), g = d(g, h, j, k, B, 5, b[28]), k = d(k, g, h, j, f, 9, b[29]), j = d(j, k, g, h, w, 14, b[30]), h = d(h, j, k, g, A, 20, b[31]), g = n(g, h, j, k, q, 4, b[32]), k = n(k, g, h, j, v, 11, b[33]), j = n(j, k, g, h, z, 16, b[34]), h = n(h, j, k, g, C, 23, b[35]), g = n(g, h, j, k, m, 4, b[36]), k = n(k, g, h, j, y, 11, b[37]), j = n(j, k, g, h, w, 16, b[38]), h = n(h, j, k, g, x, 23, b[39]), g = n(g, h, j, k, B, 4, b[40]), k = n(k, g, h, j, c, 11, b[41]), j = n(j, k, g, h, t, 16, b[42]), h = n(h, j, k, g, s, 23, b[43]), g = n(g, h, j, k, u, 4, b[44]), k = n(k, g, h, j, A, 11, b[45]), j = n(j, k, g, h, D, 16, b[46]), h = n(h, j, k, g, f, 23, b[47]), g = p(g, h, j, k, c, 6, b[48]), k = p(k, g, h, j, w, 10, b[49]), j = p(j, k, g, h, C, 15, b[50]), h = p(h, j, k, g, q, 21, b[51]), g = p(g, h, j, k, A, 6, b[52]), k = p(k, g, h, j, t, 10, b[53]), j = p(j, k, g, h, x, 15, b[54]), h = p(h, j, k, g, m, 21, b[55]), g = p(g, h, j, k, v, 6, b[56]), k = p(k, g, h, j, D, 10, b[57]), j = p(j, k, g, h, s, 15, b[58]), h = p(h, j, k, g, B, 21, b[59]), g = p(g, h, j, k, y, 6, b[60]), k = p(k, g, h, j, z, 10, b[61]), j = p(j, k, g, h, f, 15, b[62]), h = p(h, j, k, g, u, 21, b[63]); a[0] = a[0] + g | 0; a[1] = a[1] + h | 0; a[2] = a[2] + j | 0; a[3] = a[3] + k | 0 }, _doFinalize: function() { var b = this._data, e = b.words, a = 8 * this._nDataBytes, c = 8 * b.sigBytes; e[c >>> 5] |= 128 << 24 - c % 32; var m = u.floor(a / 4294967296); e[(c + 64 >>> 9 << 4) + 15] = (m << 8 | m >>> 24) & 16711935 | (m << 24 | m >>> 8) & 4278255360; e[(c + 64 >>> 9 << 4) + 14] = (a << 8 | a >>> 24) & 16711935 | (a << 24 | a >>> 8) & 4278255360; b.sigBytes = 4 * (e.length + 1); this._process(); b = this._hash; e = b.words; for (a = 0; 4 > a; a++) c = e[a], e[a] = (c << 8 | c >>> 24) & 16711935 | (c << 24 | c >>> 8) & 4278255360; return b }, clone: function() { var b = v.clone.call(this); b._hash = this._hash.clone(); return b } }); s.MD5 = v._createHelper(q); s.HmacMD5 = v._createHmacHelper(q) })(Math); (function() { var u = CryptoJS, l = u.lib, d = l.Base, n = l.WordArray, l = u.algo, p = l.EvpKDF = d.extend({ cfg: d.extend({ keySize: 4, hasher: l.MD5, iterations: 1 }), init: function(d) { this.cfg = this.cfg.extend(d) }, compute: function(d, l) { for (var p = this.cfg, v = p.hasher.create(), b = n.create(), u = b.words, r = p.keySize, p = p.iterations; u.length < r;) { e && v.update(e); var e = v.update(d) .finalize(l); v.reset(); for (var a = 1; a < p; a++) e = v.finalize(e), v.reset(); b.concat(e) } b.sigBytes = 4 * r; return b } }); u.EvpKDF = function(d, l, n) { return p.create(n) .compute(d, l) } })(); CryptoJS.lib.Cipher || function(u) { var l = CryptoJS, d = l.lib, n = d.Base, p = d.WordArray, s = d.BufferedBlockAlgorithm, q = l.enc.Base64, w = l.algo.EvpKDF, v = d.Cipher = s.extend({ cfg: n.extend(), createEncryptor: function(m, a) { return this.create(this._ENC_XFORM_MODE, m, a) }, createDecryptor: function(m, a) { return this.create(this._DEC_XFORM_MODE, m, a) }, init: function(m, a, b) { this.cfg = this.cfg.extend(b); this._xformMode = m; this._key = a; this.reset() }, reset: function() { s.reset.call(this); this._doReset() }, process: function(a) { this._append(a); return this._process() }, finalize: function(a) { a && this._append(a); return this._doFinalize() }, keySize: 4, ivSize: 4, _ENC_XFORM_MODE: 1, _DEC_XFORM_MODE: 2, _createHelper: function(m) { return { encrypt: function(f, b, e) { return (\"string\" == typeof b ? c : a) .encrypt(m, f, b, e) }, decrypt: function(f, b, e) { return (\"string\" == typeof b ? c : a) .decrypt(m, f, b, e) } } } }); d.StreamCipher = v.extend({ _doFinalize: function() { return this._process(!0) }, blockSize: 1 }); var b = l.mode = {}, x = function(a, f, b) { var c = this._iv; c ? this._iv = u : c = this._prevBlock; for (var e = 0; e < b; e++) a[f + e] ^= c[e] }, r = (d.BlockCipherMode = n.extend({ createEncryptor: function(a, f) { return this.Encryptor.create(a, f) }, createDecryptor: function(a, f) { return this.Decryptor.create(a, f) }, init: function(a, f) { this._cipher = a; this._iv = f } })) .extend(); r.Encryptor = r.extend({ processBlock: function(a, f) { var b = this._cipher, c = b.blockSize; x.call(this, a, f, c); b.encryptBlock(a, f); this._prevBlock = a.slice(f, f + c) } }); r.Decryptor = r.extend({ processBlock: function(a, b) { var c = this._cipher, e = c.blockSize, d = a.slice(b, b + e); c.decryptBlock(a, b); x.call(this, a, b, e); this._prevBlock = d } }); b = b.CBC = r; r = (l.pad = {}) .Pkcs7 = { pad: function(a, b) { for (var c = 4 * b, c = c - a.sigBytes % c, e = c << 24 | c << 16 | c << 8 | c, d = [], l = 0; l < c; l += 4) d.push(e); c = p.create(d, c); a.concat(c) }, unpad: function(a) { a.sigBytes -= a.words[a.sigBytes - 1 >>> 2] & 255 } }; d.BlockCipher = v.extend({ cfg: v.cfg.extend({ mode: b, padding: r }), reset: function() { v.reset.call(this); var a = this.cfg, c = a.iv, a = a.mode; if (this._xformMode == this._ENC_XFORM_MODE) var b = a.createEncryptor; else b = a.createDecryptor, this._minBufferSize = 1; this._mode = b.call(a, this, c && c.words) }, _doProcessBlock: function(a, c) { this._mode.processBlock(a, c) }, _doFinalize: function() { var a = this.cfg.padding; if (this._xformMode == this._ENC_XFORM_MODE) { a.pad(this._data, this.blockSize); var c = this._process(!0) } else c = this._process(!0), a.unpad(c); return c }, blockSize: 4 }); var e = d.CipherParams = n.extend({ init: function(a) { this.mixIn(a) }, toString: function(a) { return (a || this.formatter) .stringify(this) } }), b = (l.format = {}) .OpenSSL = { stringify: function(a) { var c = a.ciphertext; a = a.salt; return (a ? p.create([1398893684, 1701076831 ]) .concat(a) .concat(c) : c) .toString(q) }, parse: function(a) { a = q.parse(a); var c = a.words; if (1398893684 == c[0] && 1701076831 == c[1]) { var b = p.create(c.slice(2, 4)); c.splice(0, 4); a.sigBytes -= 16 } return e.create({ ciphertext: a, salt: b }) } }, a = d.SerializableCipher = n.extend({ cfg: n.extend({ format: b }), encrypt: function(a, c, b, d) { d = this.cfg.extend(d); var l = a.createEncryptor(b, d); c = l.finalize(c); l = l.cfg; return e.create({ ciphertext: c, key: b, iv: l.iv, algorithm: a, mode: l.mode, padding: l.padding, blockSize: a.blockSize, formatter: d.format }) }, decrypt: function(a, c, b, e) { e = this.cfg.extend(e); c = this._parse(c, e.format); return a.createDecryptor(b, e) .finalize(c.ciphertext) }, _parse: function(a, c) { return \"string\" == typeof a ? c.parse(a, this) : a } }), l = (l.kdf = {}) .OpenSSL = { execute: function(a, c, b, d) { d || (d = p.random(8)); a = w.create({ keySize: c + b }) .compute(a, d); b = p.create(a.words.slice(c), 4 * b); a.sigBytes = 4 * c; return e.create({ key: a, iv: b, salt: d }) } }, c = d.PasswordBasedCipher = a.extend({ cfg: a.cfg.extend({ kdf: l }), encrypt: function(c, b, e, d) { d = this.cfg.extend(d); e = d.kdf.execute(e, c.keySize, c.ivSize); d.iv = e.iv; c = a.encrypt.call(this, c, b, e.key, d); c.mixIn(e); return c }, decrypt: function(c, b, e, d) { d = this.cfg.extend(d); b = this._parse(b, d.format); e = d.kdf.execute(e, c.keySize, c.ivSize, b.salt); d.iv = e.iv; return a.decrypt.call(this, c, b, e.key, d) } }) }(); (function() { function u(b, a) { var c = (this._lBlock >>> b ^ this._rBlock) & a; this._rBlock ^= c; this._lBlock ^= c << b } function l(b, a) { var c = (this._rBlock >>> b ^ this._lBlock) & a; this._lBlock ^= c; this._rBlock ^= c << b } var d = CryptoJS, n = d.lib, p = n.WordArray, n = n.BlockCipher, s = d.algo, q = [57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4], w = [14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32 ], v = [1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28], b = [{ \"0\": 8421888, 268435456: 32768, 536870912: 8421378, 805306368: 2, 1073741824: 512, 1342177280: 8421890, 1610612736: 8389122, 1879048192: 8388608, 2147483648: 514, 2415919104: 8389120, 2684354560: 33280, 2952790016: 8421376, 3221225472: 32770, 3489660928: 8388610, 3758096384: 0, 4026531840: 33282, 134217728: 0, 402653184: 8421890, 671088640: 33282, 939524096: 32768, 1207959552: 8421888, 1476395008: 512, 1744830464: 8421378, 2013265920: 2, 2281701376: 8389120, 2550136832: 33280, 2818572288: 8421376, 3087007744: 8389122, 3355443200: 8388610, 3623878656: 32770, 3892314112: 514, 4160749568: 8388608, 1: 32768, 268435457: 2, 536870913: 8421888, 805306369: 8388608, 1073741825: 8421378, 1342177281: 33280, 1610612737: 512, 1879048193: 8389122, 2147483649: 8421890, 2415919105: 8421376, 2684354561: 8388610, 2952790017: 33282, 3221225473: 514, 3489660929: 8389120, 3758096385: 32770, 4026531841: 0, 134217729: 8421890, 402653185: 8421376, 671088641: 8388608, 939524097: 512, 1207959553: 32768, 1476395009: 8388610, 1744830465: 2, 2013265921: 33282, 2281701377: 32770, 2550136833: 8389122, 2818572289: 514, 3087007745: 8421888, 3355443201: 8389120, 3623878657: 0, 3892314113: 33280, 4160749569: 8421378 }, { \"0\": 1074282512, 16777216: 16384, 33554432: 524288, 50331648: 1074266128, 67108864: 1073741840, 83886080: 1074282496, 100663296: 1073758208, 117440512: 16, 134217728: 540672, 150994944: 1073758224, 167772160: 1073741824, 184549376: 540688, 201326592: 524304, 218103808: 0, 234881024: 16400, 251658240: 1074266112, 8388608: 1073758208, 25165824: 540688, 41943040: 16, 58720256: 1073758224, 75497472: 1074282512, 92274688: 1073741824, 109051904: 524288, 125829120: 1074266128, 142606336: 524304, 159383552: 0, 176160768: 16384, 192937984: 1074266112, 209715200: 1073741840, 226492416: 540672, 243269632: 1074282496, 260046848: 16400, 268435456: 0, 285212672: 1074266128, 301989888: 1073758224, 318767104: 1074282496, 335544320: 1074266112, 352321536: 16, 369098752: 540688, 385875968: 16384, 402653184: 16400, 419430400: 524288, 436207616: 524304, 452984832: 1073741840, 469762048: 540672, 486539264: 1073758208, 503316480: 1073741824, 520093696: 1074282512, 276824064: 540688, 293601280: 524288, 310378496: 1074266112, 327155712: 16384, 343932928: 1073758208, 360710144: 1074282512, 377487360: 16, 394264576: 1073741824, 411041792: 1074282496, 427819008: 1073741840, 444596224: 1073758224, 461373440: 524304, 478150656: 0, 494927872: 16400, 511705088: 1074266128, 528482304: 540672 }, { \"0\": 260, 1048576: 0, 2097152: 67109120, 3145728: 65796, 4194304: 65540, 5242880: 67108868, 6291456: 67174660, 7340032: 67174400, 8388608: 67108864, 9437184: 67174656, 10485760: 65792, 11534336: 67174404, 12582912: 67109124, 13631488: 65536, 14680064: 4, 15728640: 256, 524288: 67174656, 1572864: 67174404, 2621440: 0, 3670016: 67109120, 4718592: 67108868, 5767168: 65536, 6815744: 65540, 7864320: 260, 8912896: 4, 9961472: 256, 11010048: 67174400, 12058624: 65796, 13107200: 65792, 14155776: 67109124, 15204352: 67174660, 16252928: 67108864, 16777216: 67174656, 17825792: 65540, 18874368: 65536, 19922944: 67109120, 20971520: 256, 22020096: 67174660, 23068672: 67108868, 24117248: 0, 25165824: 67109124, 26214400: 67108864, 27262976: 4, 28311552: 65792, 29360128: 67174400, 30408704: 260, 31457280: 65796, 32505856: 67174404, 17301504: 67108864, 18350080: 260, 19398656: 67174656, 20447232: 0, 21495808: 65540, 22544384: 67109120, 23592960: 256, 24641536: 67174404, 25690112: 65536, 26738688: 67174660, 27787264: 65796, 28835840: 67108868, 29884416: 67109124, 30932992: 67174400, 31981568: 4, 33030144: 65792 }, { \"0\": 2151682048, 65536: 2147487808, 131072: 4198464, 196608: 2151677952, 262144: 0, 327680: 4198400, 393216: 2147483712, 458752: 4194368, 524288: 2147483648, 589824: 4194304, 655360: 64, 720896: 2147487744, 786432: 2151678016, 851968: 4160, 917504: 4096, 983040: 2151682112, 32768: 2147487808, 98304: 64, 163840: 2151678016, 229376: 2147487744, 294912: 4198400, 360448: 2151682112, 425984: 0, 491520: 2151677952, 557056: 4096, 622592: 2151682048, 688128: 4194304, 753664: 4160, 819200: 2147483648, 884736: 4194368, 950272: 4198464, 1015808: 2147483712, 1048576: 4194368, 1114112: 4198400, 1179648: 2147483712, 1245184: 0, 1310720: 4160, 1376256: 2151678016, 1441792: 2151682048, 1507328: 2147487808, 1572864: 2151682112, 1638400: 2147483648, 1703936: 2151677952, 1769472: 4198464, 1835008: 2147487744, 1900544: 4194304, 1966080: 64, 2031616: 4096, 1081344: 2151677952, 1146880: 2151682112, 1212416: 0, 1277952: 4198400, 1343488: 4194368, 1409024: 2147483648, 1474560: 2147487808, 1540096: 64, 1605632: 2147483712, 1671168: 4096, 1736704: 2147487744, 1802240: 2151678016, 1867776: 4160, 1933312: 2151682048, 1998848: 4194304, 2064384: 4198464 }, { \"0\": 128, 4096: 17039360, 8192: 262144, 12288: 536870912, 16384: 537133184, 20480: 16777344, 24576: 553648256, 28672: 262272, 32768: 16777216, 36864: 537133056, 40960: 536871040, 45056: 553910400, 49152: 553910272, 53248: 0, 57344: 17039488, 61440: 553648128, 2048: 17039488, 6144: 553648256, 10240: 128, 14336: 17039360, 18432: 262144, 22528: 537133184, 26624: 553910272, 30720: 536870912, 34816: 537133056, 38912: 0, 43008: 553910400, 47104: 16777344, 51200: 536871040, 55296: 553648128, 59392: 16777216, 63488: 262272, 65536: 262144, 69632: 128, 73728: 536870912, 77824: 553648256, 81920: 16777344, 86016: 553910272, 90112: 537133184, 94208: 16777216, 98304: 553910400, 102400: 553648128, 106496: 17039360, 110592: 537133056, 114688: 262272, 118784: 536871040, 122880: 0, 126976: 17039488, 67584: 553648256, 71680: 16777216, 75776: 17039360, 79872: 537133184, 83968: 536870912, 88064: 17039488, 92160: 128, 96256: 553910272, 100352: 262272, 104448: 553910400, 108544: 0, 112640: 553648128, 116736: 16777344, 120832: 262144, 124928: 537133056, 129024: 536871040 }, { \"0\": 268435464, 256: 8192, 512: 270532608, 768: 270540808, 1024: 268443648, 1280: 2097152, 1536: 2097160, 1792: 268435456, 2048: 0, 2304: 268443656, 2560: 2105344, 2816: 8, 3072: 270532616, 3328: 2105352, 3584: 8200, 3840: 270540800, 128: 270532608, 384: 270540808, 640: 8, 896: 2097152, 1152: 2105352, 1408: 268435464, 1664: 268443648, 1920: 8200, 2176: 2097160, 2432: 8192, 2688: 268443656, 2944: 270532616, 3200: 0, 3456: 270540800, 3712: 2105344, 3968: 268435456, 4096: 268443648, 4352: 270532616, 4608: 270540808, 4864: 8200, 5120: 2097152, 5376: 268435456, 5632: 268435464, 5888: 2105344, 6144: 2105352, 6400: 0, 6656: 8, 6912: 270532608, 7168: 8192, 7424: 268443656, 7680: 270540800, 7936: 2097160, 4224: 8, 4480: 2105344, 4736: 2097152, 4992: 268435464, 5248: 268443648, 5504: 8200, 5760: 270540808, 6016: 270532608, 6272: 270540800, 6528: 270532616, 6784: 8192, 7040: 2105352, 7296: 2097160, 7552: 0, 7808: 268435456, 8064: 268443656 }, { \"0\": 1048576, 16: 33555457, 32: 1024, 48: 1049601, 64: 34604033, 80: 0, 96: 1, 112: 34603009, 128: 33555456, 144: 1048577, 160: 33554433, 176: 34604032, 192: 34603008, 208: 1025, 224: 1049600, 240: 33554432, 8: 34603009, 24: 0, 40: 33555457, 56: 34604032, 72: 1048576, 88: 33554433, 104: 33554432, 120: 1025, 136: 1049601, 152: 33555456, 168: 34603008, 184: 1048577, 200: 1024, 216: 34604033, 232: 1, 248: 1049600, 256: 33554432, 272: 1048576, 288: 33555457, 304: 34603009, 320: 1048577, 336: 33555456, 352: 34604032, 368: 1049601, 384: 1025, 400: 34604033, 416: 1049600, 432: 1, 448: 0, 464: 34603008, 480: 33554433, 496: 1024, 264: 1049600, 280: 33555457, 296: 34603009, 312: 1, 328: 33554432, 344: 1048576, 360: 1025, 376: 34604032, 392: 33554433, 408: 34603008, 424: 0, 440: 34604033, 456: 1049601, 472: 1024, 488: 33555456, 504: 1048577 }, { \"0\": 134219808, 1: 131072, 2: 134217728, 3: 32, 4: 131104, 5: 134350880, 6: 134350848, 7: 2048, 8: 134348800, 9: 134219776, 10: 133120, 11: 134348832, 12: 2080, 13: 0, 14: 134217760, 15: 133152, 2147483648: 2048, 2147483649: 134350880, 2147483650: 134219808, 2147483651: 134217728, 2147483652: 134348800, 2147483653: 133120, 2147483654: 133152, 2147483655: 32, 2147483656: 134217760, 2147483657: 2080, 2147483658: 131104, 2147483659: 134350848, 2147483660: 0, 2147483661: 134348832, 2147483662: 134219776, 2147483663: 131072, 16: 133152, 17: 134350848, 18: 32, 19: 2048, 20: 134219776, 21: 134217760, 22: 134348832, 23: 131072, 24: 0, 25: 131104, 26: 134348800, 27: 134219808, 28: 134350880, 29: 133120, 30: 2080, 31: 134217728, 2147483664: 131072, 2147483665: 2048, 2147483666: 134348832, 2147483667: 133152, 2147483668: 32, 2147483669: 134348800, 2147483670: 134217728, 2147483671: 134219808, 2147483672: 134350880, 2147483673: 134217760, 2147483674: 134219776, 2147483675: 0, 2147483676: 133120, 2147483677: 2080, 2147483678: 131104, 2147483679: 134350848 }], x = [4160749569, 528482304, 33030144, 2064384, 129024, 8064, 504, 2147483679], r = s.DES = n.extend({ _doReset: function() { for (var b = this._key.words, a = [], c = 0; 56 > c; c++) { var d = q[c] - 1; a[c] = b[d >>> 5] >>> 31 - d % 32 & 1 } b = this._subKeys = []; for (d = 0; 16 > d; d++) { for (var f = b[d] = [], l = v[d], c = 0; 24 > c; c++) f[c / 6 | 0] |= a[(w[c] - 1 + l) % 28] << 31 - c % 6, f[4 + (c / 6 | 0)] |= a[28 + (w[c + 24] - 1 + l) % 28] << 31 - c % 6; f[0] = f[0] << 1 | f[0] >>> 31; for (c = 1; 7 > c; c++) f[c] >>>= 4 * (c - 1) + 3; f[7] = f[7] << 5 | f[7] >>> 27 } a = this._invSubKeys = []; for (c = 0; 16 > c; c++) a[c] = b[15 - c] }, encryptBlock: function(b, a) { this._doCryptBlock(b, a, this._subKeys) }, decryptBlock: function(b, a) { this._doCryptBlock(b, a, this._invSubKeys) }, _doCryptBlock: function(e, a, c) { this._lBlock = e[a]; this._rBlock = e[a + 1]; u.call(this, 4, 252645135); u.call(this, 16, 65535); l.call(this, 2, 858993459); l.call(this, 8, 16711935); u.call(this, 1, 1431655765); for (var d = 0; 16 > d; d++) { for (var f = c[d], n = this._lBlock, p = this._rBlock, q = 0, r = 0; 8 > r; r++) q |= b[r][((p ^ f[r]) & x[r]) >>> 0]; this._lBlock = p; this._rBlock = n ^ q } c = this._lBlock; this._lBlock = this._rBlock; this._rBlock = c; u.call(this, 1, 1431655765); l.call(this, 8, 16711935); l.call(this, 2, 858993459); u.call(this, 16, 65535); u.call(this, 4, 252645135); e[a] = this._lBlock; e[a + 1] = this._rBlock }, keySize: 2, ivSize: 2, blockSize: 2 }); d.DES = n._createHelper(r); s = s.TripleDES = n.extend({ _doReset: function() { var b = this._key.words; this._des1 = r.createEncryptor(p.create(b.slice(0, 2))); this._des2 = r.createEncryptor(p.create(b.slice(2, 4))); this._des3 = r.createEncryptor(p.create(b.slice(4, 6))) }, encryptBlock: function(b, a) { this._des1.encryptBlock(b, a); this._des2.decryptBlock(b, a); this._des3.encryptBlock(b, a) }, decryptBlock: function(b, a) { this._des3.decryptBlock(b, a); this._des2.encryptBlock(b, a); this._des1.decryptBlock(b, a) }, keySize: 6, ivSize: 2, blockSize: 2 }); d.TripleDES = n._createHelper(s) })();" +
        //  "CryptoJS.mode.ECB = (function() { var ECB = CryptoJS.lib.BlockCipherMode.extend(); ECB.Encryptor = ECB.extend({ processBlock: function(words, offset) { this._cipher.encryptBlock(words, offset); } }); ECB.Decryptor = ECB.extend({ processBlock: function(words, offset) { this._cipher.decryptBlock(words, offset); } }); return ECB; }());"+
        // "CryptoJS.pad.NoPadding = { pad: function() {}, unpad: function() {} }; CryptoJS.mode.ECB = (function() { var ECB = CryptoJS.lib.BlockCipherMode.extend(); ECB.Encryptor = ECB.extend({ processBlock: function(words, offset) { this._cipher.encryptBlock(words, offset); } }); ECB.Decryptor = ECB.extend({ processBlock: function(words, offset) { this._cipher.decryptBlock(words, offset); } }); return ECB; }());"
        //);
        //TODO 3DES
        String result = runtime.executeStringScript(
                "function decryptByDESModeCBC(ciphertext, key) { var keyHex = CryptoJS.enc.Utf8.parse(key);  var decrypted = CryptoJS.DES.decrypt({ ciphertext: CryptoJS.enc.Base64.parse(ciphertext) }, keyHex, { mode: CryptoJS.mode.ECB, padding: CryptoJS.pad.Pkcs7 }); return decrypted.toString(CryptoJS.enc.Utf8); }" +
                        "decryptByDESModeCBC(" + text + ", CryptoJS.enc.Utf8.parse(" + key + "));");
        System.out.println(result);
        runtime.release();
    }

    public String encrypt(String textToEncrypt, String password) {

        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(password, "b21332f26c793d8a");
        String cipherText = encryptor.encrypt(textToEncrypt);
        /*String decryptedText = encryptor.decrypt(cipherText);
        System.out.println("Src: " + textToEncrypt);
        System.out.println("Cipher: " + cipherText);
        Toast.makeText(getApplicationContext(), "" + cipherText, Toast.LENGTH_SHORT).show();
        System.out.println("Decrypted: " + decryptedText);
        Toast.makeText(getApplicationContext(), "" + decryptedText, Toast.LENGTH_SHORT).show();
        System.out.println("__________________");*/
        return cipherText;
    }

    public String decrypt(String textToDecrypt, String password) {

        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(password, "b21332f26c793d8a");
        String decryptedText = "";
        try {
            decryptedText = encryptor.decrypt(textToDecrypt);
        } catch (Exception e) {
            System.out.println(e.toString());

        }


        //System.out.println("Decrypted: " + decryptedText);
        return decryptedText;
    }


    public void Delete(View view) {

        Snackbar.make(findViewById(R.id.note_root), "Deleting. Please wait ", Snackbar.LENGTH_LONG).show();
        StringRequest request = new StringRequest(Request.Method.POST, SessionConstants.URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Document doc = Jsoup.parse(response);
                        Element login_ok = doc.select("div#OK").first();
                        Element login_err = doc.select("div#Err").first();

                        if (login_ok != null) {
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), Note_list3.class);
                            intent.putExtra("login", SessionConstants.login);
                            intent.putExtra("html", response);
                            intent.putExtra("URL", SessionConstants.URL);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finishAffinity();
                        } else if (login_err != null) {
                            Toast.makeText(getApplicationContext(), login_err.text(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Delete", "Delete");
                //params.put("UserName", SessionConstants.login);
                params.put("Id", Id);
                params.put("login", SessionConstants.login);
                params.put("password", SessionConstants.password);
                if (SessionConstants.global_sort_mode == 1)
                    params.put("CreateDateSort", "1");
                else if (SessionConstants.global_sort_mode == 0)
                    params.put("DeadlineDateSort", "1");
                return params;
            }
        };

        //Volley.newRequestQueue(this).add(request);
        Volley.newRequestQueue(this).add(request);
        return;

    }
}
