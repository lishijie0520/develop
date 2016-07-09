package com.lsj.cmbc.scoket;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Title : XMLMessageUtil</strong><br>
 * <strong>Description : XML���Ĺ���</strong><br>
 * <strong>Create on : 2014-6-10</strong><br>
 * 
 * @author linda1@cmbc.com.cn<br>
 */
public final class XMLMessageUtil {

	/**
	 * ��־����
	 */
	private static final Logger logger = LoggerFactory.getLogger(XMLMessageUtil.class);

	/**
	 * ����������
	 * 
	 * @param dataContainer
	 *            ��������
	 * @param messageElement
	 *            ���Ľڵ�
	 * @param parentConfigElement
	 *            �������ýڵ�
	 * @param itemNamePreffix
	 *            ����ǰ׺
	 * @param charset
	 *            �ַ���
	 */
	@SuppressWarnings("unchecked")
	public static boolean unpackField(Map<String, Object> dataContainer, Element parentMessageElement, Element configElement, String itemNamePreffix, String charset) {
		// ��ǩ��
		String tagName = configElement.getName();
		// ������
		String tagDesc = StringUtils.trimToEmpty(configElement.attributeValue("desc"));
		// Ԫ������
		String itemName = StringUtils.trimToNull(configElement.attributeValue("item"));
		try {
			if (itemName == null) {
				List<Element> subConfigElements = configElement.elements();
				if (subConfigElements != null && !subConfigElements.isEmpty()) {
					Element messageElement = parentMessageElement.element(tagName);
					if (messageElement == null) {
						List nodes = configElement.selectNodes(configElement.getPath() + "//*[@isNotEmpty=\"true\"]");
						if (nodes == null || nodes.isEmpty()) {
							logger.debug("����δ�ṩ�ڵ�[{}]", new Object[] { tagName });
							return true;
						} else {
							dataContainer.put("YHYDLX", "FAIL");
							dataContainer.put("YHYDM", "97");
							dataContainer.put("YHYDMS", String.format("��[{}]���ڲ���Ϊ�յ��ӽڵ�", tagDesc));
							return false;
						}
					}

					for (Element subConfigElement : subConfigElements) {
						if (!unpackField(dataContainer, messageElement, subConfigElement, "", charset)) {
							return false;
						}
					}
				} else {
					logger.debug("���Ľڵ�[{}]δ���ã���ִ�н���", new Object[] { tagName });
				}
				return true;
			} else if (itemName != null) {
				boolean isArray = BooleanUtils.toBoolean(configElement.attributeValue("isArray"));
				if (isArray) {
					List<Element> messageElements = parentMessageElement.elements(tagName);
					if (messageElements == null) {
						List nodes = configElement.selectNodes(configElement.getPath() + "//*[@isNotEmpty=\"true\"]");
						if (nodes == null || nodes.isEmpty()) {
							logger.debug("����δ�ṩ�ڵ�[{}]", new Object[] { tagName });
							return true;
						} else {
							dataContainer.put("YHYDLX", "FAIL");
							dataContainer.put("YHYDM", "97");
							dataContainer.put("YHYDMS", String.format("��[%s]���ڲ���Ϊ�յ��ӽڵ�", tagDesc));
							return false;
						}
					}

					// �����ӱ�ǩ
					int counter = NumberUtils.toInt((String) dataContainer.get(itemNamePreffix + itemName));
					for (Element subMessageElement : (List<Element>) parentMessageElement.elements(tagName)) {
						for (Element subConfigElement : (List<Element>) configElement.elements()) {
							if (!unpackField(dataContainer, subMessageElement, subConfigElement, itemNamePreffix + itemName + "[" + counter + "]", charset)) {
								return false;
							}
						}
					}
					counter++;
					dataContainer.put(itemNamePreffix + itemName, String.valueOf(counter));
					return true;
				}
			}

			// Ĭ��ֵ
			String defaultValue = StringUtils.trimToNull(configElement.attributeValue("default"));
			// ��ⲻ��Ϊ�ջ�մ�
			boolean isNotEmpty = BooleanUtils.toBoolean(configElement.attributeValue("isNotEmpty"));
			Element messageElement = parentMessageElement.element(tagName);
			if (messageElement == null) {
				if (isNotEmpty) {
					dataContainer.put("YHYDLX", "FAIL");
					dataContainer.put("YHYDM", "97");
					dataContainer.put("YHYDMS", String.format("��[%s]����Ϊ��", tagDesc));
					return false;
				} else {
					if (defaultValue == null) {
						logger.debug("����δ�ṩ�ڵ�[{}]", new Object[] { tagName });
						return true;
					}
				}
			}

			// ������ʽ
			String validateRegx = StringUtils.trimToNull(configElement.attributeValue("validateRegx"));
			// Դ��ֵ
			String srcValue = null;
			if (messageElement != null && (srcValue = StringUtils.trimToNull(messageElement.getText())) != null) {
				// ����
				int length = NumberUtils.toInt(configElement.attributeValue("length"));
				if (length > 0) {
					byte[] bytes = srcValue.replaceAll("\\.", "").getBytes(charset);
					if (bytes.length > length) {
						dataContainer.put("YHYDLX", "FAIL");
						dataContainer.put("YHYDM", "97");
						dataContainer.put("YHYDMS", String.format("��[%s]��ֵ������󳤶�[%d]", tagDesc, length));
						return false;
					}
				}

				if (validateRegx != null && !srcValue.matches(validateRegx)) {
					dataContainer.put("YHYDLX", "FAIL");
					dataContainer.put("YHYDM", "97");
					dataContainer.put("YHYDMS", String.format("��[%s]��ֵ[%s]У��ʧ��", tagDesc, srcValue));
					return false;
				}
			} else {
				srcValue = defaultValue;
			}

			// Ŀ����ֵ
			Object tgtValue = srcValue;
			if (srcValue != null) {

				// ��������
				String type = StringUtils.trimToNull(configElement.attributeValue("type"));
				// ��λ��������0��������λ��С��0��������λ
				int movePoint = NumberUtils.toInt(configElement.attributeValue("movePoint"));
				// ���ȣ������γɸ���ֵ
				int scale = NumberUtils.toInt(configElement.attributeValue("scale"));
				if ("integer".equalsIgnoreCase(type)) {
					BigDecimal decimal = new BigDecimal(srcValue);
					if (movePoint != 0) {
						decimal = decimal.movePointRight(movePoint);
					}
					tgtValue = decimal.intValue();
				} else if ("long".equalsIgnoreCase(type)) {
					BigDecimal decimal = new BigDecimal(srcValue);
					if (movePoint != 0) {
						decimal = decimal.movePointRight(movePoint);
					}
					tgtValue = decimal.longValue();
				} else if ("double".equalsIgnoreCase(type)) {
					BigDecimal decimal = new BigDecimal(srcValue);
					if (movePoint != 0) {
						decimal = decimal.movePointRight(movePoint);
					}
					if (scale == 0) {
						scale = 2;
					}
					decimal = decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
					tgtValue = String.valueOf(decimal.doubleValue());
				} else if ("date".equalsIgnoreCase(type)) {
					// Դ����ʱ���ʽ
					String srcFormat = StringUtils.trimToNull(configElement.attributeValue("srcFormat"));
					if (srcFormat == null) {
						srcFormat = "yyyy-MM-dd";
					}
					// Ŀ������ʱ���ʽ
					String tgtFormat = StringUtils.trimToNull(configElement.attributeValue("tgtFormat"));
					if (tgtFormat == null) {
						tgtFormat = "yyyy-MM-dd";
					}

					Date date = DateUtils.parseDate(srcValue, new String[] { srcFormat });
					tgtValue = DateFormatUtils.format(date, tgtFormat);
				} else {
					// ��ǰʱ�䣬�����ʽ�Զ�
					if (srcValue.startsWith("CUR_TIME:")) {
						String pattern = srcValue.substring("CUR_TIME:".length());
						tgtValue = DateFormatUtils.format(new Date(), pattern);
					} else if (srcValue.equals("UUID")) {
						tgtValue = uuid();
					}
				}
			}

			if (isNotEmpty && tgtValue == null) {
				dataContainer.put("YHYDLX", "FAIL");
				dataContainer.put("YHYDM", "97");
				dataContainer.put("YHYDMS", String.format("��[%s]����Ϊ��", tagDesc, srcValue));
				return false;
			}
			logger.debug("��[{}������{}����ǩ��{}]��{}", new Object[] { tagDesc, itemName, tagName, tgtValue });
			dataContainer.put(itemNamePreffix + itemName, tgtValue);
		} catch (Exception e) {
			logger.error("��[{}]���������쳣", new Object[] { tagDesc });
			logger.error(e.getLocalizedMessage(), e);
			dataContainer.put("YHYDLX", "FAIL");
			dataContainer.put("YHYDM", "97");
			dataContainer.put("YHYDMS", String.format("��[%s]���������쳣", tagDesc));
			return false;

		}
		return true;
	}

	/**
	 * ���������
	 * 
	 * @param dataContainer
	 *            ��������
	 * @param configElement
	 *            ���ýڵ�
	 * @param parentMessageElement
	 *            �����Ľڵ�
	 * @param itemNamePreffix
	 *            ����ǰ׺
	 * @param charset
	 *            �ַ���
	 */
	@SuppressWarnings("unchecked")
	public static boolean packField(Map<String, Object> dataContainer, Element configElement, Element parentMessageElement, String itemNamePreffix, String charset) {
		// ��ǩ��
		String tagName = StringUtils.trimToNull(configElement.attributeValue("id"));
		if (tagName == null) {
			tagName = configElement.getName();
		}
		// ��ǩ����
		String tagDesc = StringUtils.trimToNull(configElement.attributeValue("desc"));
		// ����
		String itemName = StringUtils.trimToNull(configElement.attributeValue("item"));
		if (itemName == null) {
			itemName = tagName;
		}
		itemName = itemNamePreffix + itemName;
		try {
			// �������
			String checkItemName = StringUtils.trimToNull(configElement.attributeValue("checkItem"));
			if (checkItemName != null) {
				String checkValue = (String) dataContainer.get(checkItemName);
				// �����ֵ�б�
				String needValues = StringUtils.trimToEmpty(configElement.attributeValue("needValues"));
				if (needValues.indexOf(checkValue + "|") < 0) {
					return true;
				}
			}

			List<Element> subConfigElements = configElement.elements();
			if (subConfigElements != null && !subConfigElements.isEmpty()) {
				int rows = NumberUtils.toInt((String) dataContainer.get(itemName));
				if (rows > 0) {
					for (int i = 0; i < rows; i++) {
						Element subMessageElement = parentMessageElement.addElement(tagName);
						for (Element subConfigElement : subConfigElements) {
							if (!packField(dataContainer, subConfigElement, subMessageElement, itemName + "[" + i + "]", charset)) {
								return false;
							}
						}
					}
				} else {
					Element subMessageElement = parentMessageElement.addElement(tagName);
					for (Element subConfigElement : subConfigElements) {
						if (!packField(dataContainer, subConfigElement, subMessageElement, "", charset)) {
							return false;
						}
					}
				}
				return true;
			}

			// ��������
			String type = StringUtils.trimToNull(configElement.attributeValue("type"));
			// ��λ��������0��������λ��С��0��������λ
			int movePoint = NumberUtils.toInt(configElement.attributeValue("movePoint"));
			// ���ȣ������γɸ���ֵ
			int scale = NumberUtils.toInt(configElement.attributeValue("scale"));
			// ��ʽ��ƥ���
			String formatRegx = StringUtils.trimToNull(configElement.attributeValue("formatRegx"));
			// ��ⲻ��Ϊ�ջ�մ�
			boolean isNotEmpty = BooleanUtils.toBoolean(configElement.attributeValue("isNotEmpty"));
			// ������ʽ
			String validateRegx = StringUtils.trimToNull(configElement.attributeValue("validateRegx"));
			// Ĭ��ֵ
			String defaultValue = StringUtils.trimToNull(configElement.attributeValue("default"));

			Object obj = dataContainer.get(itemName);
			if (obj == null) {
				obj = defaultValue;
			}

			String srcValue = String.valueOf(obj);
			// ��ǩ�ı�
			String tagText = null;
			if (obj == null || StringUtils.trimToNull(srcValue) == null) {

			} else if ("integer".equalsIgnoreCase(type)) {
				BigDecimal decimal = new BigDecimal(srcValue);
				if (movePoint != 0) {
					decimal = decimal.movePointRight(movePoint);
				}

				if (formatRegx != null) {
					tagText = formatNumber(decimal, formatRegx);
				} else {
					tagText = decimal.toPlainString();
				}
			} else if ("long".equalsIgnoreCase(type)) {
				BigDecimal decimal = new BigDecimal(srcValue);
				if (movePoint != 0) {
					decimal = decimal.movePointRight(movePoint);
				}

				if (formatRegx != null) {
					tagText = formatNumber(decimal, formatRegx);
				} else {
					tagText = decimal.toPlainString();
				}
			} else if ("double".equalsIgnoreCase(type)) {
				BigDecimal decimal = new BigDecimal(srcValue);
				if (movePoint != 0) {
					decimal = decimal.movePointRight(movePoint);
				}

				if (formatRegx != null) {
					tagText = formatNumber(decimal, formatRegx);
				} else {
					if (scale == 0) {
						scale = 2;
					}
					decimal.setScale(scale, BigDecimal.ROUND_UP);

					tagText = decimal.toPlainString();
				}
			} else if ("date".equalsIgnoreCase(type)) {
				// Դ����ʱ���ʽ
				String srcFormat = StringUtils.trimToNull(configElement.attributeValue("srcFormat"));
				if (srcFormat == null) {
					srcFormat = "yyyy-MM-dd";
				}
				// Ŀ������ʱ���ʽ
				String tgtFormat = StringUtils.trimToNull(configElement.attributeValue("tgtFormat"));
				if (tgtFormat == null) {
					tgtFormat = "yyyy-MM-dd";
				}
				Date date = null;
				if (obj instanceof java.util.Date) {
					date = (Date) obj;
				} else if (obj instanceof java.sql.Date) {
					date = new Date(((java.sql.Date) obj).getTime());
				} else if (obj instanceof java.sql.Timestamp) {
					date = new Date(((java.sql.Timestamp) obj).getTime());
				} else {
					if (srcValue.indexOf("CUR_TIME") >= 0) {
						date = new Date();
						if (srcValue.indexOf(":") > 0) {
							tgtFormat = srcValue.substring(srcValue.indexOf(":") + 1);
						}
					} else {
						date = DateUtils.parseDate(srcValue, new String[] { srcFormat });
					}
				}
				tagText = DateFormatUtils.format(date, tgtFormat);
			} else {
				tagText = srcValue;
				
				// ��ǰʱ�䣬�����ʽ�Զ�
				if (tagText.startsWith("CUR_TIME:")) {
					String pattern = tagText.substring("CUR_TIME:".length());
					tagText = DateFormatUtils.format(new Date(), pattern);
				}

				// �Ƿ���Ҫȥ��β���ո�
				String canTrim = StringUtils.trimToNull(configElement.attributeValue("canTrim"));
				if (!"false".equals(canTrim)) {
					tagText = tagText.trim();
				}

				if (tagText != null && validateRegx != null && !tagText.matches(validateRegx)) {
					dataContainer.put("YHYDLX", "FAIL");
					dataContainer.put("YHYDM", "97");
					dataContainer.put("YHYDMS", String.format("��[%s]��ֵ[%s]У��ʧ��", tagDesc, tagText));
					return false;
				}
			}

			if (tagText != null) {
				// �����ֽڳ���
				int length = NumberUtils.toInt(configElement.attributeValue("length"), 0);
				// �����ַ�����
				int charLength = NumberUtils.toInt(configElement.attributeValue("charLength"), 0);
				byte[] bytes = tagText.getBytes(charset);
				if (length > 0 && bytes.length <= length) {
					// ���뷽ʽ��left��right
					String align = StringUtils.trimToNull(configElement.attributeValue("align"));
					if (align != null) {
						// ��λ�ַ�
						String pad = StringUtils.trimToNull(configElement.attributeValue("pad"));
						if (pad == null) {
							if ("integer|long|double|".indexOf(type + "|") >= 0) {
								pad = "0";
							} else {
								pad = " ";
							}
						}

						// ����룬�ұ߲�λ
						if ("left".equalsIgnoreCase(align)) {
							tagText = StringUtils.rightPad(tagText, length, pad);
						}
						// �Ҷ��룬��߲�λ��Ĭ��ֵ
						else {
							tagText = StringUtils.leftPad(tagText, length, pad);
						}
					}
				} else if (length > 0) {
					// ���ݹ������ض�
					tagText = new String(ArrayUtils.subarray(bytes, 0, length), charset);
				} else if (length == 0) {
					if (charLength > 0 && tagText.length() <= charLength) {

					} else if (charLength > 0) {
						// ���ݹ������ض�
						tagText = tagText.substring(0, charLength);
					}
				}
			} else {
				tagText = "";
			}

			if (isNotEmpty && (tagText == null || "".equals(tagText))) {
				dataContainer.put("YHYDLX", "FAIL");
				dataContainer.put("YHYDM", "97");
				dataContainer.put("YHYDMS", String.format("��[%s]����Ϊ��", tagDesc));
				return false;
			}
			logger.debug("��[{}������{}����ǩ��{}]��{}", new Object[] { tagDesc, itemName, tagName, tagText });

			// �Ƿ�����սڵ�
			String isAllowEmptyNode = StringUtils.trimToNull(configElement.attributeValue("isAllowEmptyNode"));
			if ("false".equals(isAllowEmptyNode) && "".equals(tagText)) {
				return true;
			}
			Element messageElement = parentMessageElement.addElement(tagName);
			messageElement.setText(tagText);
		} catch (Exception e) {
			logger.error("��[{}]��������쳣", new Object[] { tagDesc });
			logger.error(e.getLocalizedMessage(), e);
			dataContainer.put("YHYDLX", "FAIL");
			dataContainer.put("YHYDM", "97");
			dataContainer.put("YHYDMS", String.format("��[%s]��������쳣", tagDesc));
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("\\-", "").toUpperCase();
	}

	/**
	 * �Ը���ֵ��������
	 * 
	 * @param value
	 *            ����ֵ
	 * @param regx
	 *            ��ʽ��ƥ���
	 * @return
	 */
	public static String formatNumber(Number value, String regx) {
		if (value == null || value.doubleValue() == 0) {
			return "";
		}
		if (regx == null && value instanceof Double) {
			regx = "#,##0.00";
		} else if (regx == null) {
			regx = "#,##0";
		}
		DecimalFormat format = new DecimalFormat(regx);
		return format.format(value);
	}
}
