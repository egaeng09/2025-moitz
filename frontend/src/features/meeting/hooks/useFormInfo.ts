import { useState } from 'react';

import {
  validateDepartureListMaxLength,
  validateStationName,
  validateDuplicateDeparture,
  validateForm,
} from '@features/meeting/utils/formValidation';

import { getMeetingStorage } from '@entities/location/model/meetingStorage';
import { LocationRequirement } from '@entities/location/types/LocationRequirement';

import { ValidationError } from '@shared/types/validationError';

type UseFormInfoReturn = {
  departureList: string[];
  conditionID: LocationRequirement;
  addDepartureWithValidation: (departure: string) => ValidationError;
  removeDepartureAtIndex: (index: number) => void;
  updateConditionID: (condition: LocationRequirement) => void;
  validateFormSubmit: () => ValidationError;
};

export function useFormInfo(): UseFormInfoReturn {
  const storage = getMeetingStorage();
  const [departureList, setDepartureList] = useState<string[]>(
    storage.departureList,
  );
  const [conditionID, setConditionID] = useState<LocationRequirement>(
    storage.conditionID,
  );

  const addDepartureWithValidation = (departure: string): ValidationError => {
    const stationNameValidation = validateStationName(departure);

    if (!stationNameValidation.isValid) {
      return stationNameValidation;
    }

    const duplicateValidation = validateDuplicateDeparture(
      departureList,
      stationNameValidation.matchedStation!,
    );

    if (!duplicateValidation.isValid) {
      return duplicateValidation;
    }

    const lengthValidation = validateDepartureListMaxLength(
      departureList.length,
    );

    if (!lengthValidation.isValid) {
      return lengthValidation;
    }

    const matchedStation = stationNameValidation.matchedStation!;
    if (!matchedStation) return;
    setDepartureList((prev) => [...prev, matchedStation]);
    return { isValid: true, message: '' };
  };

  const removeDepartureAtIndex = (index: number) => {
    setDepartureList((prev) => prev.filter((_, i) => i !== index));
  };

  const updateConditionID = (condition: LocationRequirement) => {
    setConditionID(condition);
  };

  const validateFormSubmit = (): ValidationError => {
    return validateForm(departureList, conditionID);
  };

  return {
    departureList,
    conditionID,
    addDepartureWithValidation,
    removeDepartureAtIndex,
    updateConditionID,
    validateFormSubmit,
  };
}
