package kz.sapasoft.emark.app.core;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private static final String TAG = "BLEq";
    private final Context context;
    private final BluetoothServiceCallback callback;

    private UUID serviceUUID;
    private UUID characteristicUUID;
    private UUID descriptorUUID; // Usually 00002902-0000-1000-8000-00805f9b34fb for CCCD

    private BluetoothGatt bluetoothGatt;

    public BluetoothService(Context context,
                            BluetoothServiceCallback callback) {
        this.context = context.getApplicationContext();
        this.callback = callback;
    }

    @SuppressLint("MissingPermission")
    public void searchAndConnectDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter != null ? bluetoothAdapter.getBondedDevices() : null;

        if (pairedDevices != null) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d(TAG, "Found device: " + device.getName());

                if (device.getName() != null && device.getName().contains("3M")) {
                    connect(device);
                    break;
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void connect(BluetoothDevice device) {
        Log.d(TAG, "Пытаемся подключить устройство " + device.getName().toString());
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            bluetoothGatt = device.connectGatt(context, false, gattCallback);
        }
    }

    @SuppressLint("MissingPermission")
    public void disconnectAndClose() {
        if (bluetoothGatt != null) {
            Log.d(TAG, "Disconnecting and closing GATT connection.");
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
                Log.d(TAG, "Успешное подключение, начинаем service discovery");
                gatt.discoverServices();
            } else {
                Log.e(TAG, "Ошибка соединения: статус = " + status);
                callback.onError(new Exception("Connection failed with status " + status));
                gatt.close();
                BluetoothService.this.bluetoothGatt = null;
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "Ошибка service discovery: статус = " + status);
                callback.onError(new Exception("Service discovery failed"));
                gatt.close();
                return;
            }

            for (BluetoothGattService serviceItem : gatt.getServices()) {
                Log.d(TAG, "Обнаружен сервис: " + serviceItem.getUuid());
                if (serviceItem.getUuid().toString().contains("aabb")) {
                    Log.d(TAG, "Сервис: " + serviceItem.getUuid());
                    serviceUUID = serviceItem.getUuid();
                    for (BluetoothGattCharacteristic characteristicItem : serviceItem.getCharacteristics()) {
                        if (characteristicItem.getUuid().toString().contains("1bb1")) {
                            Log.d(TAG, "  └── Характеристика: " + characteristicItem.getUuid());
                            characteristicUUID = characteristicItem.getUuid();
                        }
                    }
                }
            }

            BluetoothGattService service = gatt.getService(serviceUUID);
            if (service == null) {
                Log.e(TAG, "Сервис не найден: " + serviceUUID);
                callback.onError(new Exception("Service not found: " + serviceUUID));
                gatt.close();
                return;
            }

            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUUID);
            for (BluetoothGattDescriptor desc : characteristic.getDescriptors()) {
                Log.d(TAG, "Найден дескриптор: " + desc.getUuid());
                descriptorUUID = desc.getUuid();
            }

            if (characteristic == null) {
                Log.e(TAG, "Характеристика не найдена: " + characteristicUUID);
                callback.onError(new Exception("Characteristic not found: " + characteristicUUID));
                gatt.close();
                return;
            }

            boolean readInitiated = gatt.readCharacteristic(characteristic);
            if (!readInitiated) {
                Log.e(TAG, "Не удалось инициировать чтение характеристики");
                callback.onError(new Exception("Failed to initiate characteristic read"));
                gatt.close();
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (characteristic.getUuid().equals(characteristicUUID)) { // Check it's our target
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    byte[] value = characteristic.getValue();
                    String stringValue = new String(value); // Consider Charset
                    callback.onDiscovered(stringValue);
                } else {
                    Log.e(TAG, "Ошибка чтения характеристики: " + status);
                    callback.onError(new Exception("Characteristic read failed with status " + status));
                }
                // 2. After read (success or fail), proceed to enable notifications
                enableNotificationsProcedure(gatt, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(characteristicUUID)) { // Check if it's our target
                byte[] value = characteristic.getValue();
                String stringValue = new String(value); // Consider Charset
                Log.d(TAG, "Уведомление получено (onCharacteristicChanged): " + stringValue);
                callback.onChanged(
                        characteristic.getService().getUuid(),
                        characteristic.getUuid(),
                        stringValue
                );
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (descriptor.getCharacteristic().getUuid().equals(characteristicUUID) &&
                    descriptor.getUuid().equals(descriptorUUID)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.i(TAG, "Уведомления/Индикации успешно включены для " + descriptor.getCharacteristic().getUuid());
                    // Connection is now fully set up for notifications.
                } else {
                    Log.e(TAG, "Ошибка записи дескриптора: статус = " + status);
                    callback.onError(new Exception("Descriptor write failed with status " + status));
                    // Potentially close gatt here
                }
            }
        }

        @SuppressLint("MissingPermission")
        private void enableNotificationsProcedure(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (!characteristic.getUuid().equals(characteristicUUID)) return; // Not our target

            int props = characteristic.getProperties();
            if (!((props & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0 || (props & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0)) {
                Log.e(TAG, "Характеристика не поддерживает уведомления или индикации");
                callback.onError(new Exception("Characteristic doesn't support notifications/indications"));
                // Potentially close gatt here if notifications are essential
                return;
            }

            boolean notificationSet = gatt.setCharacteristicNotification(characteristic, true);
            if (!notificationSet) {
                Log.e(TAG, "Не удалось включить уведомления (setCharacteristicNotification)");
                callback.onError(new Exception("Failed to set notification locally"));
                return;
            }

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(descriptorUUID);
            if (descriptor == null) {
                Log.e(TAG, "Дескриптор CCCD (2902) не найден");
                callback.onError(new Exception("CCCD descriptor not found"));
                return;
            }

            // Determine if it's NOTIFY or INDICATE
            byte[] descriptorValue;
            if ((props & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                descriptorValue = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE;
                Log.d(TAG, "Enabling indications for " + characteristic.getUuid());
            } else {
                descriptorValue = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
                Log.d(TAG, "Enabling notifications for " + characteristic.getUuid());
            }

            descriptor.setValue(descriptorValue);
            boolean descriptorWriteInitiated = gatt.writeDescriptor(descriptor);
            if (!descriptorWriteInitiated) {
                Log.e(TAG, "Не удалось инициировать запись дескриптора");
                callback.onError(new Exception("Failed to initiate descriptor write"));
            } else {
                Log.d(TAG, "Write descriptor initiated for " + characteristic.getUuid());
            }
            // Result of write will come to onDescriptorWrite
        }
    };
}