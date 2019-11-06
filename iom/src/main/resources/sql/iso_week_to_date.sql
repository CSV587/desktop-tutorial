CREATE OR REPLACE FUNCTION iso_week_to_date
  (iso_week_year varchar2)
  RETURN DATE
IS
  jan4_of_iso_year DATE;
  first_day_of_iso_year DATE;
  iso_date DATE;
  iso_date_iso_year INTEGER;
BEGIN
  jan4_of_iso_year := TO_DATE(substr(iso_week_year,1,4) || '-01-04', 'YYYY-MM-DD');
  first_day_of_iso_year := TRUNC(jan4_of_iso_year, 'IW');
  iso_date := first_day_of_iso_year + 7 * (substr(iso_week_year,6,2) - 1);
  iso_date_iso_year := TO_CHAR(iso_date, 'IYYY');
  IF iso_date_iso_year <> substr(iso_week_year,1,4) THEN
    RAISE VALUE_ERROR;
  END IF;
  RETURN iso_date;
END;