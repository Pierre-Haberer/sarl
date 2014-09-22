/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.eclipse.launch.sre;

import io.sarl.eclipse.util.PluginUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jdt.launching.PropertyChangeEvent;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Standard SRE install.
 * <p>
 * The standard SRE install assumes: <ul>
 * <li>The SRE is based on a single JAR file.</li>
 * <li>The main class of the SRE is defined in the Manifest field <code>"Main-Class"</code>.</li>
 * <li>The Manifest contains a section named <code>"SARL-Runtime-Environment"</code>. This
 *     section contains the following entries: <ul>
 *     <li>The version number of the SARL sepcifications that are supported by the SRE is
 *     defined in the Manifest field <code>"SARL-Spec-Version"</code>.</li>
 *     <li>The name of the SRE may be given by the field <code>"Name"</code>.</li>
 *     <li>The VM arguments of the SRE may be given by the field <code>"VM-Arguments"</code>.</li>
 *     <li>The program arguments of the SRE may be given by the field <code>"Program-Arguments"</code>.</li>
 *     </ul></li>
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StandardSREInstall extends AbstractSREInstall {

	private IPath jarFile;
	private String vmArguments = PluginUtil.EMPTY_STRING;
	private String programArguments = PluginUtil.EMPTY_STRING;

	private String manifestMainClass;
	private String manifestName;

	/** Construct a SRE installation.
	 *
	 * @param id - the identifier of this SRE installation.
	 */
	public StandardSREInstall(String id) {
		super(id);
	}

	@Override
	public StandardSREInstall clone() {
		StandardSREInstall clone = (StandardSREInstall) super.clone();
		clone.jarFile = this.jarFile == null ? null
				: Path.fromPortableString(clone.jarFile.toPortableString());
		return clone;
	}

	@Override
	public StandardSREInstall copy(String id) {
		return (StandardSREInstall) super.copy(id);
	}

	/** Replies the path to the JAR file that is supporting this SRE installation.
	 *
	 * @return the path to the JAR file. Must not be <code>null</code>.
	 */
	public IPath getJarFile() {
		return this.jarFile;
	}

	/** Change the path to the JAR file that is supporting this SRE installation.
	 *
	 * @param jarFile - the path to the JAR file. Must not be <code>null</code>.
	 */
	public void setJarFile(IPath jarFile) {
		assert (jarFile != null);
		if (!PluginUtil.equals(jarFile, this.jarFile)) {
			PropertyChangeEvent event = new PropertyChangeEvent(
					this, ISREInstallChangedListener.PROPERTY_JAR_FILE,
					this.jarFile, jarFile);
			this.jarFile = jarFile;
			setDirty(true);
			if (getNotify()) {
				SARLRuntime.fireSREChanged(event);
			}
		}
	}

	@Override
	public String getLocation() {
		IPath jarFile = getJarFile();
		if (jarFile == null) {
			return getName();
		}
		return jarFile.toOSString();
	}

	@Override
	public String getName() {
		String n = super.getName();
		if (n == null || n.isEmpty()) {
			IPath p = getJarFile();
			if (p != null) {
				n = p.removeFileExtension().lastSegment();
			} else {
				n = getId();
			}
		}
		return n;
	}

	@Override
	public String getNameNoDefault() {
		return super.getName();
	}

	private static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	@Override
	protected void resolveDirtyFields(boolean forceSettings) {
		assert (this.jarFile != null);
		try (JarFile jFile = new JarFile(this.jarFile.toFile())) {
			Manifest manifest = jFile.getManifest();
			//
			// Main class
			this.manifestMainClass = manifest.getMainAttributes().getValue("Main-Class"); //$NON-NLS-1$
			if (this.manifestMainClass == null || this.manifestMainClass.isEmpty()) {
				throw new SREException("Invalid main class for SRE with id " + getId()); //$NON-NLS-1$
			}
			if (forceSettings || isEmpty(getMainClass())) {
				setMainClass(this.manifestMainClass);
			}
			// Get SARL section:
			Attributes sarlSection = manifest.getAttributes("SARL-Runtime-Environment"); //$NON-NLS-1$
			if (sarlSection == null) {
				throw new SREException("SARL runtime environment section not found in the manifest."); //$NON-NLS-1$
			}
			//
			// SARL version
			String sarlVersion = sarlSection.getValue("SARL-Spec-Version"); //$NON-NLS-1$
			String minVersion = null;
			String maxVersion = null;
			if (sarlVersion != null && !sarlVersion.isEmpty()) {
				try {
					Version sarlVer = Version.parseVersion(sarlVersion);
					if (sarlVer != null) {
						minVersion = new Version(sarlVer.getMajor(), sarlVer.getMinor(), 0).toString();
						maxVersion = new Version(sarlVer.getMajor(), sarlVer.getMinor() + 1, 0).toString();
					}
				} catch (Throwable _) {
					//
				}
			}
			if (forceSettings || isEmpty(getMinimalSARLVersion())) {
				setMinimalSARLVersion(minVersion);
			}
			if (forceSettings || isEmpty(getMaximalSARLVersion())) {
				setMaximalSARLVersion(maxVersion);
			}
			//
			// SRE Name
			this.manifestName = unnullify(sarlSection.getValue("SRE-Name")); //$NON-NLS-1$
			if (forceSettings || isEmpty(getNameNoDefault())) {
				setName(this.manifestName);
			}
			//
			// VM arguments
			String vmArgs = sarlSection.getValue("VM-Arguments"); //$NON-NLS-1$
			if (!PluginUtil.equalsString(vmArgs, this.vmArguments)) {
				PropertyChangeEvent event = new PropertyChangeEvent(
						this, ISREInstallChangedListener.PROPERTY_VM_ARGUMENTS,
						this.vmArguments, unnullify(vmArgs));
				this.vmArguments = unnullify(vmArgs);
				if (getNotify()) {
					SARLRuntime.fireSREChanged(event);
				}
			}
			//
			// VM-specific attributes
			setVMSpecificAttributesMap(null);
			//
			// Program arguments
			String programArgs = sarlSection.getValue("Program-Arguments"); //$NON-NLS-1$
			if (!PluginUtil.equalsString(programArgs, this.programArguments)) {
				PropertyChangeEvent event = new PropertyChangeEvent(
						this, ISREInstallChangedListener.PROPERTY_PROGRAM_ARGUMENTS,
						this.programArguments, unnullify(programArgs));
				this.programArguments = unnullify(programArgs);
				if (getNotify()) {
					SARLRuntime.fireSREChanged(event);
				}
			}
			//
			// Library Location
			if (forceSettings) {
				LibraryLocation location = new LibraryLocation(this.jarFile, Path.EMPTY, Path.EMPTY);
				setLibraryLocations(location);
			}
		} catch (SREException e) {
			throw e;
		} catch (Throwable e) {
			throw new SREException(e);
		}

	}

	@Override
	public String getProgramArguments() {
		if (this.programArguments.isEmpty()) {
			return getMainClass();
		}
		return getMainClass() + " " + this.programArguments; //$NON-NLS-1$
	}

	@Override
	public String getVMArguments() {
		return this.vmArguments;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isValidInstallation() {
		try {
			IPath path = getJarFile();
			if (path == null) {
				return false;
			}
			File file = path.toFile();
			if (file == null || !file.canRead()) {
				return false;
			}
		} catch (Throwable _) {
			return false;
		}
		return super.isValidInstallation();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getAsXML(Document document, Element element) throws IOException {
		IPath path = getJarFile();
		element.setAttribute("libraryPath", path.toPortableString()); //$NON-NLS-1$
		String name = getName();
		if (!PluginUtil.equalsString(name, this.manifestName)) {
			element.setAttribute("name", name); //$NON-NLS-1$
		}
		String mainClass = getMainClass();
		if (!PluginUtil.equals(mainClass, this.manifestMainClass)) {
			element.setAttribute("mainClass", mainClass); //$NON-NLS-1$
		}
		LibraryLocation[] libraries = getLibraryLocations();
		if (libraries.length != 1 || !libraries[0].getSystemLibraryPath().equals(this.jarFile)) {
			for (LibraryLocation location : libraries) {
				Element libraryNode = document.createElement("libraryLocation"); //$NON-NLS-1$
				libraryNode.setAttribute("systemLibraryPath", location.getSystemLibraryPath().toPortableString()); //$NON-NLS-1$
				libraryNode.setAttribute("packageRootPath", location.getPackageRootPath().toPortableString()); //$NON-NLS-1$
				libraryNode.setAttribute("sourcePath", location.getSystemLibrarySourcePath().toPortableString()); //$NON-NLS-1$
				URL javadoc = location.getJavadocLocation();
				if (javadoc != null) {
					libraryNode.setAttribute("javadoc", javadoc.toString()); //$NON-NLS-1$
				}
			}
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setFromXML(Element element) throws IOException {
		IPath path = parsePath(element.getAttribute("libraryPath"), null); //$NON-NLS-1$
		try {
			if (path != null) {
				setJarFile(path);

				String name = element.getAttribute("name"); //$NON-NLS-1$
				if (name != null && !name.isEmpty()) {
					setName(name);
				}
				String mainClass = element.getAttribute("mainClass"); //$NON-NLS-1$
				if (mainClass != null && !mainClass.isEmpty()) {
					setMainClass(mainClass);
				}

				List<LibraryLocation> locations = new ArrayList<>();
				NodeList children = element.getChildNodes();
				for (int i = 0; i < children.getLength(); ++i) {
					Node node = children.item(i);
					if (node instanceof Element && "libraryLocation".equalsIgnoreCase(node.getNodeName())) { //$NON-NLS-1$
						Element libraryNode = (Element) node;
						IPath systemLibraryPath = parsePath(
								libraryNode.getAttribute("systemLibraryPath"), null); //$NON-NLS-1$
						if (systemLibraryPath != null) {
							IPath packageRootPath = parsePath(
									libraryNode.getAttribute("packageRootPath"), Path.EMPTY); //$NON-NLS-1$
							IPath sourcePath = parsePath(
									libraryNode.getAttribute("sourcePath"), Path.EMPTY); //$NON-NLS-1$
							URL javadoc = null;
							try {
								String urlTxt = libraryNode.getAttribute("javadoc"); //$NON-NLS-1$
								javadoc = new URL(urlTxt);
							} catch (Throwable _) {
								//
							}
							LibraryLocation location = new LibraryLocation(
									systemLibraryPath, sourcePath, packageRootPath,
									javadoc);
							locations.add(location);
						} else {
							PluginUtil.logErrorMessage("Invalid XML format for the SRE " + getId()); //$NON-NLS-1$
						}
					}
				}

				if (!locations.isEmpty()) {
					LibraryLocation[] tab = locations.toArray(new LibraryLocation[locations.size()]);
					setLibraryLocations(tab);
				}

				return;
			}
		} catch (Throwable _) {
			//
		}
		throw new IOException("Invalid library path for the SRE " + getId()); //$NON-NLS-1$
	}

	private static IPath parsePath(String path, IPath defaultPath) {
		if (path != null && !path.isEmpty()) {
			try {
				IPath p = Path.fromPortableString(path);
				if (p != null) {
					return p;
				}
			} catch (Throwable _) {
				//
			}
		}
		return defaultPath;
	}

}
