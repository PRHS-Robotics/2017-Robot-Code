import java.nio.ByteBuffer;

public class main {
	public static void main(String[] args){
		byte[] rawBytes = new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
		ByteBuffer.wrap(rawBytes).putDouble(12.34576262);
		boolean b = true;
		//put boolean in the lsb of the double
		if (b){
			rawBytes[7] |= 1 << 0;
		}else{
			rawBytes[7]  &= ~(1 << 0);
		}
		double num = ByteBuffer.wrap(rawBytes).getDouble();
		System.out.println("Number: "+num);
		System.out.println("Boolean: "+((rawBytes[7]&0x01)==0x01));
	}
}
