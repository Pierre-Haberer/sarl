/**
 */
package io.sarl.lang.sarl;

import org.eclipse.xtext.common.types.JvmParameterizedTypeReference;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Behavior Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link io.sarl.lang.sarl.BehaviorUnit#getName <em>Name</em>}</li>
 *   <li>{@link io.sarl.lang.sarl.BehaviorUnit#getGuard <em>Guard</em>}</li>
 *   <li>{@link io.sarl.lang.sarl.BehaviorUnit#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @see io.sarl.lang.sarl.SarlPackage#getBehaviorUnit()
 * @model
 * @generated
 */
public interface BehaviorUnit extends Feature
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' containment reference.
   * @see #setName(JvmParameterizedTypeReference)
   * @see io.sarl.lang.sarl.SarlPackage#getBehaviorUnit_Name()
   * @model containment="true"
   * @generated
   */
  JvmParameterizedTypeReference getName();

  /**
   * Sets the value of the '{@link io.sarl.lang.sarl.BehaviorUnit#getName <em>Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' containment reference.
   * @see #getName()
   * @generated
   */
  void setName(JvmParameterizedTypeReference value);

  /**
   * Returns the value of the '<em><b>Guard</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Guard</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Guard</em>' containment reference.
   * @see #setGuard(XExpression)
   * @see io.sarl.lang.sarl.SarlPackage#getBehaviorUnit_Guard()
   * @model containment="true"
   * @generated
   */
  XExpression getGuard();

  /**
   * Sets the value of the '{@link io.sarl.lang.sarl.BehaviorUnit#getGuard <em>Guard</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Guard</em>' containment reference.
   * @see #getGuard()
   * @generated
   */
  void setGuard(XExpression value);

  /**
   * Returns the value of the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Body</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' containment reference.
   * @see #setBody(XExpression)
   * @see io.sarl.lang.sarl.SarlPackage#getBehaviorUnit_Body()
   * @model containment="true"
   * @generated
   */
  XExpression getBody();

  /**
   * Sets the value of the '{@link io.sarl.lang.sarl.BehaviorUnit#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(XExpression value);

} // BehaviorUnit
