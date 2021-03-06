package net.lang.gpuimage.filter.advanced;

import android.content.Context;
import android.opengl.GLES20;

import net.lang.gpuimage.R;
import net.lang.gpuimage.filter.base.gpuimage.GPUImageFilter;
import net.lang.gpuimage.utils.OpenGlUtils;

public class MagicXproIIFilter extends GPUImageFilter{
	private int[] inputTextureHandles = {-1,-1};
	private int[] inputTextureUniformLocations = {-1,-1};
	private int mGLStrengthLocation;
	private Context context;

	public MagicXproIIFilter(Context context){
		super(NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.xproii_filter_shader, context));
		this.context = context;
	}
	
	public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(inputTextureHandles.length, inputTextureHandles, 0);
        for(int i = 0; i < inputTextureHandles.length; i++)
        	inputTextureHandles[i] = -1;
        context = null;
    }
	
	protected void onDrawArraysAfter(){
		for(int i = 0; i < inputTextureHandles.length
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0+i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		}
	}
	  
	protected void onDrawArraysPre(){
		for(int i = 0; i < inputTextureHandles.length 
				&& inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++){
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0+i);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
			GLES20.glUniform1i(inputTextureUniformLocations[i], i + 1);
		}
	}
	
	public void onInit(){
		super.onInit();
		inputTextureHandles[0] = OpenGlUtils.loadTexture(context, "filter/xpromap.png");
		inputTextureHandles[1] = OpenGlUtils.loadTexture(context, "filter/vignettemap_new.png");
		mGLStrengthLocation = GLES20.glGetUniformLocation(mGLProgId,
				"strength");
	}

	protected void onInitialized() {
		super.onInitialized();
        setFloat(mGLStrengthLocation, 1.0f);
	}
}
