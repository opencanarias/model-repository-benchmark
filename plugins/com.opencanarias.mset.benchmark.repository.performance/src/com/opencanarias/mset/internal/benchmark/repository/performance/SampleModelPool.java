package com.opencanarias.mset.internal.benchmark.repository.performance;

import org.eclipse.emf.common.util.URI;

public class SampleModelPool {

	private static final String BUNDLE_PATH = "/com.opencanarias.mset.benchmark.repository.performance";

	private static URI getPlatformURI(String path) {
		return URI.createPlatformPluginURI(BUNDLE_PATH + path, false);
	}
	
	public static URI get1ModelURI() {
		return getPlatformURI("/samples/size-1/model_1_elements_0.emfbin");
	}

	public static URI get10ModelURI() {
		return getPlatformURI("/samples/size-10/model_10_elements_0.emfbin");
	}
	
	public static URI get100ModelURI() {
		return getPlatformURI("/samples/size-100/model_100_elements_0.emfbin");
	}
	
	public static URI get1KModelURI() {
		return getPlatformURI("/samples/size-1k/model_1k_elements_0.emfbin");
	}

	public static URI get10KModelURI() {
		return getPlatformURI("/samples/size-10k/model_10k_elements_0.emfbin");
	}

	public static URI get100KModelURI() {
		return getPlatformURI("/samples/size-100k/model_100k_elements_0.emfbin");
	}

}
