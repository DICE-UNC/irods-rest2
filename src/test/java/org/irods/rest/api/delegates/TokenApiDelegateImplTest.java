package org.irods.rest.api.delegates;

import java.util.Properties;

import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.rest.utils.RestTestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TokenApiDelegateImplTest {

	private static Properties testingProperties = new Properties();
	private static Properties restTestingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static RestTestUtils restTestUtils = new RestTestUtils();
	private static org.irods.jargon.testutils.filemanip.ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "TokenApiDelegateImplTest";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		restTestingProperties = restTestUtils.getRestTestProperties();
		scratchFileUtils = new org.irods.jargon.testutils.filemanip.ScratchFileUtils(testingProperties);
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testIssueJwt() throws Exception {

		/*
		 * IRODSAccount irodsAccount =
		 * testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties
		 * ); List<GrantedAuthority> granted = new ArrayList<>(); GrantedAuthority
		 * grantedAuthority = new SimpleGrantedAuthority("authToken");
		 * granted.add(grantedAuthority); IrodsAuthentication authToken = new
		 * IrodsAuthentication(irodsAccount.getUserName(), "authToken", granted);
		 * SecurityContextHolder.getContext().setAuthentication(authToken);
		 * IrodsRestConfiguration irodsRestConfiguration = restTestUtils
		 * .irodsRestConfigurationFromTestProperties(testingProperties,
		 * restTestingProperties); TokenApiDelegateImpl tokenApiDelegate = new
		 * TokenApiDelegateImpl();
		 * tokenApiDelegate.setIrodsAccessObjectFactory(irodsFileSystem.
		 * getIRODSAccessObjectFactory());
		 * tokenApiDelegate.setIrodsRestConfiguration(irodsRestConfiguration);
		 * 
		 * ResponseEntity<String> jwt = tokenApiDelegate.obtainToken();
		 * Assert.assertFalse("no jwt returned", jwt.getBody().isEmpty());
		 */

	}

}
